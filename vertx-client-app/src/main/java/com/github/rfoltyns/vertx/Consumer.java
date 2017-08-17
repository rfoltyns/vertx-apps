package com.github.rfoltyns.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

public class Consumer extends AbstractVerticle {

    public void start(Future<Void> startFuture) {
        vertx.eventBus().consumer("consumer1", message -> {
            System.out.println("1 received message.body() = "
                    + message.body());
        });
    }

}
