package com.github.rfoltyns.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerResponse;

public class VertxHttpServer extends AbstractVerticle {

    private HttpServer httpServer = null;
    private DeliveryOptions defaultDeliveryOptions = new DeliveryOptions()
            .setCodecName(ServerMessageConsumerCodec.class.getName())
            .setSendTimeout(30000);

    @Override
    public void start() throws Exception {
        vertx.eventBus().registerCodec(new ServerMessageConsumerCodec());
        vertx.eventBus().registerCodec(new ServerMessageProducerCodec());
        httpServer = vertx.createHttpServer(new HttpServerOptions()
                .setUsePooledBuffers(true)
        );

        httpServer.requestHandler(request -> {
            request.bodyHandler(
                    bodyBuffer -> {
                        vertx.eventBus().send(
                                "consumer1",
                                bodyBuffer,
                                defaultDeliveryOptions,
                                (Handler<AsyncResult<Message<Buffer>>>) event -> {
                                        HttpServerResponse response = request.response();
                                        response.headers().add("Content-Length", String.valueOf(event.result().body().length()));
                                        response.write(event.result().body());
                                        response.end();
                        });
                    }
            );
        });

        httpServer.listen(8080);
    }

}
