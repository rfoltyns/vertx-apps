package com.github.rfoltyns.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

public class Consumer extends AbstractVerticle {

    private final DeliveryOptions deliveryOptions = new DeliveryOptions()
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

            if (message.getDelayMillis() > 0) {
                int delay = message.getDelayMillis() +
                        (random.nextInt(2) == 0 ?
                                message.getDelayDeviation() : -message.getDelayDeviation());

                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            event.reply(message, deliveryOptions);
        });
    }

}
