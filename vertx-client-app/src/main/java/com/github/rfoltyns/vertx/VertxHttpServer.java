package com.github.rfoltyns.vertx;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.rfoltyns.stats.SocketPublisher;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.ErrorHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.BridgeEventType;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import io.vertx.ext.web.impl.RouterImpl;

import java.io.IOException;

public class VertxHttpServer extends AbstractVerticle {

    private HttpServer httpServer = null;

    @Override
    public void start() throws Exception {
        httpServer = vertx.createHttpServer(new HttpServerOptions().setUsePooledBuffers(true));

        Router router = new RouterImpl(vertx);
        router.route().handler(BodyHandler.create());
        router.route("/static/*").handler(StaticHandler.create("webroot").setCachingEnabled(false));

        router.get("/").handler(ctx -> {
            ctx.reroute("/static/index.html");
        });

        router.post("/schedule").handler(event -> {
                System.out.println("incoming request!");
                ScheduleRequest scheduleRequest;
                try {
                    scheduleRequest = Json.mapper.readValue(event.getBody().getBytes(), ScheduleRequest.class);
                    vertx.eventBus().send(scheduleRequest.getConsumer(), Json.mapper.writeValueAsString(scheduleRequest));
                    System.out.println("body: " + scheduleRequest);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                HttpServerResponse response = event.response();
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
                System.out.println("A socket was created");
            }
            event.complete(true);
        });
    }

    private ErrorHandler errorHandler() {
        return ErrorHandler.create();
    }

    private Router graphRouter() {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());

        router.route().consumes("application/json");
        router.route().produces("application/json");

        return router;
    }
}
