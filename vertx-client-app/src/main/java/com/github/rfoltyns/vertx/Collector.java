package com.github.rfoltyns.vertx;

import com.github.rfoltyns.stats.ResultCollector;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;

import java.io.IOException;

public class Collector extends AbstractVerticle {

    public static class CollectorHolder {
        public static final ResultCollector INSTANCE =
                new ResultCollector(new double[]{10, 20, 30, 40, 50, 60, 70, 80, 90, 95, 99, 99.9, 99.99, 99.999})
                        .refreshEachMillis(100)
                        .printEachMillis(1000);

    }

    @Override
    public void start() throws Exception {
        vertx.eventBus().consumer("resultCollector", (Message<Buffer> message) -> {
            try {
                CollectorHolder.INSTANCE.add(Json.mapper.readValue(message.body().getBytes(), ClientMessage.class));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}