package com.github.rfoltyns.vertx;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.json.Json;
import org.apache.commons.math.stat.descriptive.rank.Percentile;
import org.apache.juli.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class VertxClient extends AbstractVerticle {

    private final Logger console = LogManager.getLogger(VertxClient.class);

    private HttpClient httpClient;
    private AtomicInteger counter = new AtomicInteger();
    private Collection<ClientMessage> resultList = new ConcurrentLinkedQueue();

    private Timer timer = new Timer();

    @Override
    public void start() throws Exception {
        httpClient = vertx.createHttpClient(
                new HttpClientOptions()
                        .setPipelining(true)
                        .setPipeliningLimit(20)
                        .setMaxPoolSize(100)
                        .setKeepAlive(true)
                .setReuseAddress(true)
                .setUsePooledBuffers(true)
        );
        vertx.eventBus().consumer("client1", (Message<String> message) -> {
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
                byte[] bytes;
                try {
                    bytes = Json.mapper.writeValueAsBytes(clientMessage);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }

                request.headers().add("Content-Length", String.valueOf(bytes.length));
                request.handler(httpClientResponse -> httpClientResponse.bodyHandler(body -> {
                    try {
                        ClientMessage response = Json.mapper.readValue(body.getBytes(), ClientMessage.class);
                        response.setProcessedAt(System.currentTimeMillis());
                        resultList.add(response);

                        console.debug("{}", response);
                    } catch (IOException e) {
                        console.error(e.getMessage());
                        throw new RuntimeException(e);
                    }
                    httpClientResponse.headers().add("Content-Length", String.valueOf(body.length()));
                }))
                .write(Buffer.buffer(bytes))
                .end();
            }

            printStats(scheduleRequest.getNumberOfRequests());

            console.info("Messages created in {}ms", System.currentTimeMillis() - start);

        });


    }

    private void printStats(int numberOfRequests) {
        timer.schedule(createTask(), numberOfRequests / 10 + 1000);
    }

    private TimerTask createTask() {
        return new TimerTask() {
            @Override
            public void run() {
                if (resultList.size() > 0) {
                    console.info("\nRequests sent: {}", counter.get());
                    console.info("\nPercentiles[10, 20, 30, 40, 50, 60, 70, 80, 90, 95, 99, 99.9, 99.99, 99.999]");

                    double[] percentiles = new double[]{10, 20, 30, 40, 50, 60, 70, 80, 90, 95, 99, 99.9, 99.99, 99.999};

                    StringBuilder sb = new StringBuilder(512);
                    sb.append("\nAvg. time to client;");
                    for (double percentile : percentiles) {
                        sb.append(getReceivedAvg(percentile)).append(";");
                    }

                    sb.append("\nAvg. response time;");
                    for (double percentile : percentiles) {
                        sb.append(getProcessedAvg(percentile)).append(";");
                    }

                    sb.append("\nAvg. service time;");
                    for (double percentile : percentiles) {
                        sb.append(getServiceTime(percentile, 0, resultList.size())).append(";");
                    }
                    console.info(sb.toString());

                    resultList.clear();
                }
            }
        };
    }

    private double getReceivedAvg(double percentile) {
        double[] values = new double[resultList.size()];

        int index = 0;
        for (ClientMessage message : resultList) {
            values[index++] = (message.getReceivedAt() - message.getCreatedAt());
        }

        return toPrecision(2, new Percentile().evaluate(values, percentile));
    }

    private double toPrecision(double percentile, double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_EVEN);
        return bd.doubleValue();
    }

    private double getProcessedAvg(double percentile) {
        double[] values = new double[resultList.size()];
        int index = 0;
        for (ClientMessage message : resultList) {
            values[index++] = (message.getProcessedAt() - message.getCreatedAt());
        }
        return toPrecision(2, new Percentile().evaluate(values, percentile));
    }

    private double getServiceTime(double percentile, int begin, int end) {
        int index = 0;
        double[] values = new double[resultList.size()];
        for (ClientMessage message : resultList) {
            values[index++] = (message.getProcessedAt() - message.getReceivedAt());
        }
        return toPrecision(2, new Percentile().evaluate(values, percentile));
    }

}
