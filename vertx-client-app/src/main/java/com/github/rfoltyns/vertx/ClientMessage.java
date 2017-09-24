package com.github.rfoltyns.vertx;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public class ClientMessage {

    private final int id;
    private  String consumer;
    private Long createdAt;
    private Long receivedAt;
    private Long processedAt;
    private int delayMillis;
    private int delayDeviation;
    private int numberOfHops;
    private byte[] data;

    @JsonCreator
    public ClientMessage(@JsonProperty("id") int id) {
        this.id = id;
        this.createdAt = System.currentTimeMillis();
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

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getProcessedAt() {
        return processedAt;
    }

    public Long getReceivedAt() {
        return receivedAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public void setProcessedAt(Long processedAt) {
        this.processedAt = processedAt;
    }

    public void setReceivedAt(Long receivedAt) {
        this.receivedAt = receivedAt;
    }

    public int getDelayMillis() {
        return delayMillis;
    }

    public void setDelayMillis(int delayMillis) {
        this.delayMillis = delayMillis;
    }

    public int getDelayDeviation() {
        return delayDeviation;
    }

    public void setDelayDeviation(int delayDeviation) {
        this.delayDeviation = delayDeviation;
    }

    public int getNumberOfHops() {
        return numberOfHops;
    }

    public void setNumberOfHops(int numberOfHops) {
        this.numberOfHops = numberOfHops;
    }

    public String metrics() {
        return new StringBuilder(64)
                .append("{\"messageId\":").append(id)
                .append(",\"timestamp\":\"").append(Instant.ofEpochMilli(createdAt).toString()).append("\"")
                .append(",\"receivedIn\": ").append(receivedAt - createdAt)
                .append(",\"processedIn\": ").append(processedAt - createdAt)
                .append("}")
                .toString();
    }

    @Override
    public String toString() {
        return new StringBuilder(256)
                .append("Message[id=").append(id)
                .append(", createdAt: ").append(createdAt)
                .append(", receivedAt: ").append(receivedAt)
                .append(", processedAt: ").append(processedAt)
                .append(", delayMillis: ").append(delayMillis)
                .append(", delayDeviation: ").append(delayDeviation)
                .append("]")
                .toString();
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
