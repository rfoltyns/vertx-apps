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

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class VertxLoadClient extends AbstractVerticle {

    private HttpClient httpClient;
    private AtomicInteger counter = new AtomicInteger();

    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(500);
    private static Queue<ScheduledFuture<?>> scheduledFutures = new ConcurrentLinkedQueue<>();
    private static int numberOfRequestsPerThread = 100;
    private Random random = new Random();

    public static class CollectorHolder {
        public static final ResultCollector INSTANCE =
                new ResultCollector(new double[]{10, 20, 30, 40, 50, 60, 70, 80, 90, 95, 99, 99.9, 99.99, 99.999})
                        .refreshEachMillis(100)
                        .printEachMillis(1000);
    }

    @Override
    public void start() throws Exception {
        httpClient = vertx.createHttpClient(
                new HttpClientOptions()
//                        .setPipelining(true)
//                        .setPipeliningLimit(50)
                        .setMaxPoolSize(500)
                        .setTcpNoDelay(true)
                        .setKeepAlive(true)
                        .setReuseAddress(true)
                        .setUsePooledBuffers(true)
        );

        vertx.eventBus().consumer("load", (Message<String> message) -> {
            ScheduleRequest scheduleRequest;
            scheduleRequest = deserialize(message, ScheduleRequest.class);

            long start = System.currentTimeMillis();

            for (int ii = 0; ii < scheduleRequest.getNumberOfThreads(); ii++) {
                scheduleSendRequestTask(scheduleRequest);
            }

            if (scheduleRequest.getNumberOfThreads() < 0) {
                cancelRunningTasks(scheduleRequest);
            } else {
                System.out.println("Messages created in " + (System.currentTimeMillis() - start + "ms"));
            }

            CollectorHolder.INSTANCE.setRequestsPerSecond(scheduledFutures.size() * numberOfRequestsPerThread);
        });

    }

    private void sendRequest(ScheduleRequest scheduleRequest) {
        HttpClientRequest request = httpClient.post(8080, "localhost", "/");
        ClientMessage clientMessage = new ClientMessage(counter.incrementAndGet());
        clientMessage.setDelayMillis(scheduleRequest.getDelayInMillis());
        clientMessage.setDelayDeviation(scheduleRequest.getDelayDeviation());
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
//                    System.out.println(response.metrics());
                } catch  (IOException e) {
                    System.out.println(e.getMessage());
                    throw new RuntimeException(e);
                }
                httpClientResponse.headers().add("Content-Length", String.valueOf(body.length()));
            });
        })
        .write(Buffer.buffer(bytes))
        .end();
    }

    private void cancelRunningTasks(ScheduleRequest scheduleRequest) {
        for (int ii = 0; ii < Math.abs(scheduleRequest.getNumberOfThreads()); ii++) {
            if (scheduledFutures.size() > 0)
                scheduledFutures.poll().cancel(false);
        }
    }

    private void scheduleSendRequestTask(ScheduleRequest scheduleRequest) {
        Runnable runnable = () -> {
            for (int n = 0; n < numberOfRequestsPerThread; n++) {
                sendRequest(scheduleRequest);
                try {
                    Thread.currentThread().sleep(random.nextInt(19) + 1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };

        ScheduledFuture<?> scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(
                runnable,
                1L,
                1L,
                TimeUnit.SECONDS);

        scheduledFutures.add(scheduledFuture);
    }

    private <T> T deserialize(Message<String> message, Class<T> targetClass) {
        try {
            return Json.mapper.readValue(message.body(), targetClass);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
