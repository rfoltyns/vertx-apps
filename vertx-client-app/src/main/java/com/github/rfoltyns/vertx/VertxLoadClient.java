package com.github.rfoltyns.vertx;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.rfoltyns.stats.ResultCollector;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.json.Json;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class VertxLoadClient extends AbstractVerticle {

    private final Logger console = LogManager.getLogger(VertxLoadClient.class);

    private HttpClient httpClient;
    private AtomicInteger counter = new AtomicInteger();

    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(500);
    private static Queue<ScheduledFuture<?>> scheduledFutures = new ConcurrentLinkedQueue<>();
    private static int numberOfRequestsPerThread = 100;
    private Random random = new Random();

    private DeliveryOptions resultDeliverOptions = new DeliveryOptions()
            .setCodecName(CollectorMessageCodec.class.getSimpleName());

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
                .setIdleTimeout(30000)
        );
        vertx.eventBus().registerCodec(new CollectorMessageCodec());

        vertx.eventBus().consumer("load", (Message<String> message) -> {
            ScheduleRequest scheduleRequest =  deserialize(message, ScheduleRequest.class);

            for (int ii = 0; ii < scheduleRequest.getNumberOfThreads(); ii++) {
                scheduleSendRequestTask(scheduleRequest);
            }

            if (scheduleRequest.getNumberOfThreads() < 0) {
                cancelRunningTasks(scheduleRequest);
            } else {
                console.info("Request scheduled: ", scheduleRequest);
            }
        });

    }

    private void sendRequest(ScheduleRequest scheduleRequest) {
        ClientMessage clientMessage = new ClientMessage(counter.incrementAndGet());
        clientMessage.setDelayMillis(scheduleRequest.getDelayInMillis());
        clientMessage.setDelayDeviation(scheduleRequest.getDelayDeviation());
        clientMessage.setConsumer(scheduleRequest.getConsumer());
        clientMessage.setNumberOfHops(scheduleRequest.getNumberOfHops());

        byte[] data = new byte[scheduleRequest.getMessageSizeInBytes()];
        random.nextBytes(data);
        clientMessage.setData(data);

        byte[] bytes;
        try {
            bytes = Json.mapper.writeValueAsBytes(clientMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        HttpClientRequest request = httpClient.post(8080, "localhost", "/");
        request.setTimeout(10000);
        request.headers().add("Content-Type", "application/json");
        request.headers().add("Content-Length", String.valueOf(bytes.length));
        request.handler(httpClientResponse -> {
            httpClientResponse.bodyHandler(body -> {
                try {
                    ClientMessage response = Json.mapper.readValue(body.getBytes(), ClientMessage.class);
                    response.setProcessedAt(System.currentTimeMillis());
                    vertx.eventBus().publish("resultCollector", response, resultDeliverOptions);
                } catch  (IOException e) {
                    console.error(e.getMessage());
                    throw new RuntimeException(e);
                }
                httpClientResponse.headers().add("Content-Length", String.valueOf(body.length()));
            }).exceptionHandler(new Handler<Throwable>() {
                @Override
                public void handle(Throwable event) {
                    console.error(event.getMessage());
                }
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
                    break;
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
            console.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
