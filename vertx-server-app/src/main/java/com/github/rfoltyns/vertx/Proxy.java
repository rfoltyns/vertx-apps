package com.github.rfoltyns.vertx;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.json.Json;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class Proxy extends AbstractVerticle {

    private final Logger console = LogManager.getLogger(Proxy.class);

    private HttpClient httpClient;
//    private WebClient httpClient;

    @Override
    public void start() throws Exception {
//        httpClient = WebClient.create(vertx, new WebClientOptions()
//                .setMaxPoolSize(250)
//                .setTcpNoDelay(true)
//                .setKeepAlive(true)
//                .setReuseAddress(true)
//                .setUsePooledBuffers(true)
//                .setIdleTimeout(30000)
//        );
        httpClient = vertx.createHttpClient(
                new HttpClientOptions()
                        .setPipelining(true)
                        .setPipeliningLimit(50)
                        .setMaxPoolSize(500)
                        .setTcpNoDelay(true)
                        .setKeepAlive(true)
                        .setReuseAddress(true)
                        .setUsePooledBuffers(true)
                        .setIdleTimeout(30000)
        );

        vertx.eventBus().consumer("proxy", (Message<ServerMessage> event) -> {
            ServerMessage serverMessage = event.body();
            serverMessage.setNumberOfHops(serverMessage.getNumberOfHops() - 1);

            HttpClientRequest request = httpClient.post(8081, "localhost", "/");
            Handler<HttpClientResponse> responseHandler = asyncResponse -> {
                Handler<Buffer> responseBodyHandler = response -> {
                    event.headers().add("Content-Length", String.valueOf(response.length()));
                    event.reply(response, new DeliveryOptions().setCodecName(ServerMessageBufferCodec.class.getName()));
                };
                asyncResponse.bodyHandler(responseBodyHandler);
            };

            request.headers().add("Content-Type", "application/json");
            request.setTimeout(10000);
            Buffer buffer = Buffer.buffer().appendBytes(serializeToBytes(serverMessage));
            request.headers().add("Content-Length", String.valueOf(buffer.length()));
            request.handler(responseHandler);
            request.write(buffer).end();
//            request.handler(httpClientResponse -> {
//                httpClientResponse.bodyHandler(responseBody -> {
//                    httpClientResponse.headers().add("Content-Length", String.valueOf(responseBody.length()));
//                    event.reply(Buffer.buffer(responseBody.getBytes()));
//                }).exceptionHandler(new Handler<Throwable>() {
//                    @Override
//                    public void handle(Throwable event) {
//                        console.error(event.getMessage());
//                    }
//                });
//            })
//            .exceptionHandler(exceptionContext -> {
//                console.error("Send error: " + exceptionContext.getMessage());
//            });
        }).exceptionHandler(exceptionContext -> {
            console.error("Consume error: " + exceptionContext.getMessage());
        });
    }

    private byte[] serializeToBytes(ServerMessage serverMessage) {
        try {
            return Json.mapper.writeValueAsBytes(serverMessage);
        } catch (JsonProcessingException e) {
            console.error(serverMessage.getClass().getSimpleName() + " mapping error", e.getMessage());
            return new byte[0];
        }
    }


    private <T> T deserialize(byte[] payload, Class<T> targetClass) {
        try {
            return Json.mapper.readValue(payload, targetClass);
        } catch (IOException e) {
            console.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
