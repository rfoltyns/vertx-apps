package com.github.rfoltyns.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.Json;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import static io.vertx.core.http.HttpHeaders.CONTENT_LENGTH;

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

        vertx.eventBus().consumer("consumer", (Handler<Message<ServerMessage>>) event -> {
            counter.incrementAndGet();
            ServerMessage message = event.body();
            message.setReceivedAt(System.currentTimeMillis());

            if (message.getConsumer() != null && message.getNumberOfHops() > 0) {
                vertx.eventBus().send(message.getConsumer(),
                        JsonUtils.encodeToBuffer(message),
                        requestDeliveryOptions,
                        (AsyncResult<Message<Buffer>> asyncResult) -> {
                            event.headers().add(CONTENT_LENGTH, String.valueOf(asyncResult.result().body().length()));
                            event.reply(JsonUtils.decodeValue(asyncResult.result().body(), ServerMessage.class), responseDeliveryOptions);
                        });
            } else if (message.getDelayMillis() > 0) {
                delay(event, message);
            } else {
                event.reply(message, responseDeliveryOptions);
            }

        });
    }

    private void delay(Message<ServerMessage> event, ServerMessage message) {
        int millis = message.getDelayMillis()
                + (random.nextInt(2) == 0 ? message.getDelayDeviation() : -message.getDelayDeviation());

        vertx.executeBlocking(future -> {
            sleep(millis);
            future.complete(JsonUtils.encodeToBuffer(message));
        }, result -> {
            if (result.failed()) {
                System.out.println("Something went wrong..");
            }
            if (result.succeeded()) {
                event.reply(message, responseDeliveryOptions);
            }
        });
    }

    private void sleep(int delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
