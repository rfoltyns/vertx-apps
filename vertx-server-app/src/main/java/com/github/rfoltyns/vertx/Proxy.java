package com.github.rfoltyns.vertx;

import io.vertx.core.AbstractVerticle;
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

public class Proxy extends AbstractVerticle {

    private final Logger console = LogManager.getLogger(Proxy.class);

    private HttpClient httpClient;

    private DeliveryOptions deliveryOptions = new DeliveryOptions()
            .setCodecName(ServerMessageBufferCodec.class.getName());

    @Override
    public void start() throws Exception {

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
                    event.reply(response, deliveryOptions);
                };
                asyncResponse.bodyHandler(responseBodyHandler);
            };

            request.headers().add("Content-Type", "application/json");
            request.setTimeout(10000);
            Buffer buffer = JsonUtils.encodeToBuffer(serverMessage);
            request.headers().add("Content-Length", String.valueOf(buffer.length()));
            request.handler(responseHandler);
            request.write(buffer).end();

        }).exceptionHandler(exceptionContext -> {
            console.error("Consume error: " + exceptionContext.getMessage());
        });
    }

}
