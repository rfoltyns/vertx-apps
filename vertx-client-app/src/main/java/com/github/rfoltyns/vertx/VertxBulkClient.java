package com.github.rfoltyns.vertx;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.rfoltyns.stats.ResultCollector;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.json.Json;
import org.apache.commons.math.stat.descriptive.rank.Percentile;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class VertxBulkClient extends AbstractVerticle {

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
                        .setPipelining(true)
                        .setPipeliningLimit(20)
                        .setMaxPoolSize(500)
                        .setKeepAlive(true)
                        .setReuseAddress(true)
                        .setUsePooledBuffers(true)
        );
        vertx.eventBus().consumer("bulk", (Message<String> message) -> {
            ScheduleRequest scheduleRequest;
            try {
                scheduleRequest = Json.mapper.readValue(message.body(), ScheduleRequest.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            long start = System.currentTimeMillis();

            for (int ii = 0; ii < scheduleRequest.getNumberOfRequests(); ii++) {

                HttpClientRequest request = httpClient.post(8080, "localhost", "/");

                ClientMessage clientMessage = new ClientMessage(counter.incrementAndGet());
                clientMessage.setDelayMillis(scheduleRequest.getDelayInMillis());
                byte[] bytes;
                try {
                    bytes = Json.mapper.writeValueAsBytes(clientMessage);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }

                request.headers().add("Content-Type", "application/json");
                request.headers().add("Content-Length", String.valueOf(bytes.length));
                request.handler(httpClientResponse -> {
                    httpClientResponse.bodyHandler(body -> {
                        try {
                            ClientMessage response = Json.mapper.readValue(body.getBytes(), ClientMessage.class);
                            response.setProcessedAt(System.currentTimeMillis());
                            CollectorHolder.INSTANCE.add(response);

                            System.out.println(response.metrics());
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                            throw new RuntimeException(e);
                        }
                        httpClientResponse.headers().add("Content-Length", String.valueOf(body.length()));
                    });
                })
                .setTimeout(clientMessage.getDelayMillis() + 1000)
                .write(Buffer.buffer(bytes))
                .end();
            }

            System.out.println("Messages created in " + (System.currentTimeMillis() - start + "ms"));

        });


    }

}
