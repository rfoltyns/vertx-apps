package com.github.rfoltyns.vertx;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

public class Consumer extends AbstractVerticle {

    private final Logger console = LogManager.getLogger(Proxy.class);

    private final DeliveryOptions requestDeliveryOptions = new DeliveryOptions()
            .setCodecName(ServerMessageConsumerCodec.class.getName());

    private final DeliveryOptions responseDeliveryOptions = new DeliveryOptions()
            .setCodecName(ServerMessageProducerCodec.class.getName());

    private static Random random = new Random();

    private static AtomicInteger counter = new AtomicInteger();
    private static Timer timer = new Timer();
    private static TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            if (counter.get() > 0)
                System.out.println("Requests handled last second: " + counter.getAndSet(0));
        }
    };

    static {
        timer.scheduleAtFixedRate(timerTask,1000,1000);
    }

    public void start(Future<Void> startFuture) {

        vertx.eventBus().consumer("consumer1", (Handler<Message<ServerMessage>>) event -> {
            counter.incrementAndGet();
            ServerMessage message = event.body();
            message.setReceivedAt(System.currentTimeMillis());

            if (message.getConsumer() != null && message.getNumberOfHops() > 0) {
//                console.info("Passing through. Port: " + System.getProperty("vertx-server-port"));
                vertx.eventBus().send(message.getConsumer(),
                        Buffer.buffer(serialize(message)),
                        requestDeliveryOptions,
                        (AsyncResult<Message<Buffer>> asyncResult) -> {
                            event.headers().add("Content-Length", String.valueOf(asyncResult.result().body().length()));
                            event.reply(deserialize(asyncResult.result().body().getBytes(), ServerMessage.class), responseDeliveryOptions);
                        });
            } else if (message.getDelayMillis() > 0) {
                int millis = message.getDelayMillis()
                        + (random.nextInt(2) == 0 ? message.getDelayDeviation() : -message.getDelayDeviation());

                vertx.executeBlocking(future -> {
                    delay(millis);
                    future.complete(Buffer.buffer(serialize(message)));
                }, result -> {
                    if (result.failed()) {
                        System.out.println("Something went wrong..");
                    }
                    if (result.succeeded()) {
                        event.reply(message, responseDeliveryOptions);
                    }
                });
            } else {
                event.reply(message, responseDeliveryOptions);
            }

        });
    }

    private <T> T deserialize(byte[] payload, Class<T> targetClass) {
        try {
            return Json.mapper.readValue(payload, targetClass);
        } catch (IOException e) {
            console.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private byte[] serialize(ServerMessage message) {
        try {
            return Json.mapper.writeValueAsBytes(message);
        } catch (JsonProcessingException e) {
            console.error("Mapping error", e.getMessage());
            return new byte[0];
        }
    }

    private void delay(int delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
