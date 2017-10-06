package com.github.rfoltyns.vertx;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.*;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.metrics.Measured;
import io.vertx.ext.dropwizard.MetricsService;
import io.vertx.ext.dropwizard.impl.AbstractMetrics;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.impl.RouterImpl;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

public class VertxHttpServer extends AbstractVerticle {

    private HttpServer httpServer = null;
    private DeliveryOptions defaultDeliveryOptions = new DeliveryOptions()
            .setCodecName(ServerMessageConsumerCodec.class.getName())
            .setSendTimeout(30000);

    @Override
    public void start() throws Exception {
        vertx.eventBus().registerCodec(new ServerMessageConsumerCodec());
        vertx.eventBus().registerCodec(new ServerMessageProducerCodec());
        vertx.eventBus().registerCodec(new ServerMessageBufferCodec());
        httpServer = vertx.createHttpServer(new HttpServerOptions()
                .setUsePooledBuffers(true)
        );

        MetricsService metricsService = MetricsService.create(vertx);

        Router router = new RouterImpl(vertx);
        router.route().handler(BodyHandler.create());
        router.route().handler(CorsHandler.create("*")
                .allowedMethod(HttpMethod.GET)
                .allowedMethod(HttpMethod.POST)
                .allowedMethod(HttpMethod.OPTIONS)
                .allowedHeader("Content-Type")
                .allowedHeader("Origin"));

        router.get("/metrics").handler(event ->  {
            String metricsJson = Json.encode(metricsService.getMetricsSnapshot(vertx));
            event.response().headers().add("Content-Type", "application/json");
            event.response().headers().add("Content-Length", String.valueOf(metricsJson.length()));
            event.response().write(metricsJson);
            event.response().end();
        });

        router.post("/").handler(routingContext -> {
                Handler<AsyncResult<Message<Buffer>>> asyncResultHandler = getAsyncResultHandler(routingContext.request());
                vertx.eventBus().send(
                        "consumer1",
                        routingContext.getBody(),
                        defaultDeliveryOptions,
                        asyncResultHandler);
        });

        httpServer.requestHandler(router::accept).listen(Integer.valueOf(System.getProperty("vertx-server-port", "8080")));
    }

    private Handler<AsyncResult<Message<Buffer>>> getAsyncResultHandler(HttpServerRequest request) {
        return event -> {
            HttpServerResponse response = request.response();
            response.headers().add("Content-Length", String.valueOf(event.result().body().length()));
            response.end(event.result().body());
        };
    }

}
