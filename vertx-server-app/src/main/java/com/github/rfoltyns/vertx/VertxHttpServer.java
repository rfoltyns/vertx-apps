package com.github.rfoltyns.vertx;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
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

        httpServer.requestHandler(request -> {
            request.bodyHandler(
                    bodyBuffer -> {
                        Handler<AsyncResult<Message<Buffer>>> asyncResultHandler = getAsyncResultHandler(request);
                        vertx.eventBus().send(
                                "consumer1",
                                bodyBuffer,
                                defaultDeliveryOptions,
                                asyncResultHandler);
                    }
            );
        });

        httpServer.listen(Integer.valueOf(System.getProperty("vertx-server-port", "8080")));
    }

    private Handler<AsyncResult<Message<Buffer>>> getAsyncResultHandler(HttpServerRequest request) {
        return event -> {
            HttpServerResponse response = request.response();
            response.headers().add("Content-Length", String.valueOf(event.result().body().length()));
            response.end(event.result().body());
        };
    }

    private byte[] serializeToBytes(ServerMessage serverMessage) {
        try {
            return Json.mapper.writeValueAsBytes(serverMessage);
        } catch (JsonProcessingException e) {
            System.out.println(serverMessage.getClass().getSimpleName() + " mapping error: " +  e.getMessage());
            return new byte[0];
        }
    }

}
