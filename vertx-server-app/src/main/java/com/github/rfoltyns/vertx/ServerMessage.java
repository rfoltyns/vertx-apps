package com.github.rfoltyns.vertx;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ServerMessage {

    private final int id;
    private String consumer;
    private long createdAt;
    private long receivedAt;
    private long processedAt;
    private int delayMillis;
    private int delayDeviation;
    private int numberOfHops;

    @JsonCreator
    public ServerMessage(@JsonProperty("id") int id, @JsonProperty("createdAt") long createdAt) {
        this.id = id;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public String getConsumer() {
        return consumer;
    }

    public void setConsumer(String consumer) {
        this.consumer = consumer;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getProcessedAt() {
        return processedAt;
    }

    public long getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(long receivedAt) {
        this.receivedAt = receivedAt;
    }

    public void setProcessedAt(long processedAt) {
        this.processedAt = processedAt;
    }

    public int getDelayMillis() {
        return delayMillis;
    }

    public int getDelayDeviation() {
        return delayDeviation;
    }

    public int getNumberOfHops() {
        return numberOfHops;
    }

    public void setNumberOfHops(int numberOfHops) {
        this.numberOfHops = numberOfHops;
    }
}
