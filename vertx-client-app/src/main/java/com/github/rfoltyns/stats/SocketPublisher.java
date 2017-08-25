package com.github.rfoltyns.stats;

import io.vertx.core.Vertx;
import io.vertx.core.json.Json;

import java.io.IOException;

public class SocketPublisher implements StatsListener {

    private final String target;
    private final Vertx vertx;

    public SocketPublisher(String target, Vertx vertx) {
        this.target = target;
        this.vertx = vertx;
    }

    @Override
    public String getTarget() {
        return target;
    }

    /**
     * All stats listeners created for the same target are equal
     * @param o
     * @return true if this.target == o.target, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        SocketPublisher that = (SocketPublisher) o;
        return target.equals(that.target);
    }

    @Override
    public int hashCode() {
        return target.hashCode();
    }

    @Override
    public void notify(Percentiles snapshot) throws IOException {
        this.vertx.eventBus().publish(target, Json.mapper.writeValueAsString(snapshot));
    }

}
