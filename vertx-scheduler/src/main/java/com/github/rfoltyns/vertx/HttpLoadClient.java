package com.github.rfoltyns.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpVersion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static io.vertx.core.http.HttpHeaders.CONTENT_LENGTH;
import static io.vertx.core.http.HttpHeaders.CONTENT_TYPE;

public class HttpLoadClient extends AbstractVerticle {

    private final Logger console = LogManager.getLogger(HttpLoadClient.class);

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
//                        .setTryUseCompression(true)
                        .setProtocolVersion(HttpVersion.HTTP_2)
                        .setDefaultHost("localhost")
                        .setDefaultPort(8088)
                        .setHttp2MultiplexingLimit(100)
                        .setPipelining(true)
                        .setPipeliningLimit(500)
                        .setReuseAddress(true)
                        .setUsePooledBuffers(true)
                .setIdleTimeout(30000)
        );
        vertx.eventBus().registerCodec(new CollectorMessageCodec());

        vertx.eventBus().consumer("httpload", (Message<Buffer> message) -> {
            ScheduleRequest scheduleRequest =  JsonUtils.decodeValue(message.body(), ScheduleRequest.class);

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

        Buffer buffer = JsonUtils.encodeToBuffer(createClientMessage(scheduleRequest));

        HttpClientRequest request = httpClient.post("/");
        request.setTimeout(10000);
        request.headers().add(CONTENT_TYPE, "application/json");
        request.headers().add(CONTENT_LENGTH, String.valueOf(buffer.length()));
        request.handler(httpClientResponse -> {
            httpClientResponse.bodyHandler(body -> {

                ClientMessage response = JsonUtils.decodeValue(body, ClientMessage.class);
                response.setProcessedAt(System.currentTimeMillis());

                Collector.CollectorHolder.INSTANCE.add(response);
//                vertx.eventBus().publish("resultCollector", response, resultDeliverOptions);

                httpClientResponse.headers().add(CONTENT_LENGTH, String.valueOf(body.length()));
            }).exceptionHandler(event -> console.error(event.getMessage()));
        })
        .write(buffer)
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

    private ClientMessage createClientMessage(ScheduleRequest scheduleRequest) {
        ClientMessage clientMessage = new ClientMessage(counter.incrementAndGet());
        clientMessage.setDelayMillis(scheduleRequest.getDelayInMillis());
        clientMessage.setDelayDeviation(scheduleRequest.getDelayDeviation());
        clientMessage.setConsumer(scheduleRequest.getConsumer());
        clientMessage.setNumberOfHops(scheduleRequest.getNumberOfHops());

        byte[] data = new byte[scheduleRequest.getMessageSizeInBytes()];
        random.nextBytes(data);
        clientMessage.setData(data);

        return clientMessage;
    }

}
