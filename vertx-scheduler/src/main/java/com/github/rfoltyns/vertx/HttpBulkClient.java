package com.github.rfoltyns.vertx;

import com.github.rfoltyns.stats.ResultCollector;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpHeaders;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicInteger;

public class HttpBulkClient extends AbstractVerticle {

    private final Logger console = LogManager.getLogger(HttpBulkClient.class);

    private HttpClient httpClient;
    private AtomicInteger counter = new AtomicInteger();

    private static class CollectorHolder {
        public static ResultCollector INSTANCE =
                new ResultCollector(new double[]{10, 20, 30, 40, 50, 60, 70, 80, 90, 95, 99, 99.9, 99.99, 99.999})
                        .refreshEachMillis(100)
                        .printEachMillis(1000);
    }

    @Override
    public void start() throws Exception {
        httpClient = vertx.createHttpClient(
                new HttpClientOptions()
//                        .setPipelining(true)
//                        .setPipeliningLimit(20)
                        .setMaxPoolSize(5000)
                        .setKeepAlive(true)
                        .setUsePooledBuffers(true)
        );
        vertx.eventBus().consumer("bulk", (Message<String> message) -> {
            ScheduleRequest scheduleRequest = JsonUtils.decodeValue(message.body(), ScheduleRequest.class);

            long start = System.currentTimeMillis();

            for (int ii = 0; ii < scheduleRequest.getNumberOfRequests(); ii++) {
                Buffer buffer = JsonUtils.encodeToBuffer(createClientMessage(scheduleRequest));

                HttpClientRequest request = httpClient.post(8088, "localhost", "/");
                request.headers().add(HttpHeaders.CONTENT_TYPE, "application/json");
                request.headers().add(HttpHeaders.CONTENT_LENGTH, String.valueOf(buffer.length()));
                request.handler(httpClientResponse -> {
                    httpClientResponse.bodyHandler(body -> {
                        ClientMessage response = JsonUtils.decodeValue(body, ClientMessage.class);
                        response.setProcessedAt(System.currentTimeMillis());
                        CollectorHolder.INSTANCE.add(response);
//                        console.debug(response.metrics());
                        httpClientResponse.headers().add("Content-Length", String.valueOf(body.length()));
                    });
                })
                .setTimeout(5000)
                .write(buffer)
                .end();
            }

            console.info("Messages created in {}ms", System.currentTimeMillis() - start);

        });

    }

    private ClientMessage createClientMessage(ScheduleRequest scheduleRequest) {
        ClientMessage clientMessage = new ClientMessage(counter.incrementAndGet());
        clientMessage.setConsumer(scheduleRequest.getConsumer());
        clientMessage.setNumberOfHops(scheduleRequest.getNumberOfHops());
        clientMessage.setDelayMillis(scheduleRequest.getDelayInMillis());
        return clientMessage;
    }

}
