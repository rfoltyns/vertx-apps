package com.github.rfoltyns.vertx;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.rfoltyns.stats.SocketPublisher;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.ErrorHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.BridgeEventType;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import io.vertx.ext.web.impl.RouterImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;


public class VertxHttpServer extends AbstractVerticle {

    private HttpServer httpServer = null;
    private Logger console = LogManager.getLogger(VertxHttpServer.class);

    @Override
    public void start() throws Exception {
        httpServer = vertx.createHttpServer(new HttpServerOptions().setUsePooledBuffers(true));

        Router router = new RouterImpl(vertx);
        router.route().handler(BodyHandler.create());
        router.route().handler(CorsHandler.create("*")
                .allowedMethod(HttpMethod.GET)
                .allowedMethod(HttpMethod.POST)
                .allowedMethod(HttpMethod.OPTIONS)
                .allowedHeader("Content-Type")
                .allowedHeader("Origin"));

        router.route("/static/*").handler(StaticHandler.create("webroot").setCachingEnabled(false));

        router.get("/").handler(ctx -> {
            ctx.reroute("/static/index.html");
        });

        router.post("/schedule").handler(request -> {
                console.info("Processing schedule request");
                ScheduleRequest scheduleRequest;
                try {
                    scheduleRequest = Json.mapper.readValue(request.getBody().getBytes(), ScheduleRequest.class);
                    vertx.eventBus().send(scheduleRequest.getType(), Json.mapper.writeValueAsString(scheduleRequest));
                    console.info("Scheduled request: {}", scheduleRequest);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                HttpServerResponse response = request.response();
                response.headers().add("Content-Length", String.valueOf("Done".length()));
                response.write("Done");
                response.end();
            }
        );

        router.route("/eventbus/*").handler(eventBusHandler());

        router.post("/graph/:metricsType")
                .consumes("application/json")
                .produces("application/json")
                .handler(routingContext -> {
                    String metricsType = routingContext.request().getParam("metricsType");
                    try {
                        JsonNode body = Json.mapper.readTree(routingContext.getBodyAsString());
                        String graphName = body.get("graphName").asText();
                        boolean reset = body.has("reset") ? body.get("reset").asBoolean() : false;

                        if (reset) {
                            Collector.CollectorHolder.INSTANCE.reset();
                        }
                        if ("realtime".equals(metricsType)) {
                            Collector.CollectorHolder.INSTANCE.addSnapshotListener(new SocketPublisher(graphName, vertx));
                        } else {
                            Collector.CollectorHolder.INSTANCE.addSummaryListener(new SocketPublisher(graphName, vertx));
                        }
                    } catch (IOException e) {
                        new RuntimeException(e.getMessage(), e);
                    }
                });

        router.patch("/graph/:metricsType")
                .consumes("application/json")
                .produces("application/json")
                .handler(routingContext -> {
                    Collector.CollectorHolder.INSTANCE.reset();
                });

        router.delete("/graph/:metricsType")
                .consumes("application/json")
                .produces("application/json")
                .handler(routingContext -> {
                    String metricsType = routingContext.request().getParam("metricsType");
                    try {
                        String graphName = Json.mapper.readTree(routingContext.getBodyAsString()).get("graphName").asText();
                        if ("realtime".equals(metricsType)) {
                            Collector.CollectorHolder.INSTANCE.removeSnapshotListenerByName(graphName);
                        } else {
                            Collector.CollectorHolder.INSTANCE.removeSummaryListenerByName(graphName);
                        }
                    } catch (IOException e) {
                        new RuntimeException(e.getMessage(), e);
                    }
                });

        router.route().failureHandler(errorHandler());

        httpServer.requestHandler(router::accept).listen(9999);

    }

    private SockJSHandler eventBusHandler() {
        BridgeOptions options = new BridgeOptions()
                .addOutboundPermitted(new PermittedOptions().setAddressRegex("graph.*"));
        return SockJSHandler.create(vertx).bridge(options, event -> {
            if (event.type() == BridgeEventType.SOCKET_CREATED) {
                console.info("Socket created");
            }
            event.complete(true);
        });
    }

    private ErrorHandler errorHandler() {
        return ErrorHandler.create();
    }

}
