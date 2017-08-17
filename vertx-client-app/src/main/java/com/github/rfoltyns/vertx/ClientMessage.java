package com.github.rfoltyns.vertx;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ClientMessage {

    private final int id;
    private Long createdAt;
    private Long receivedAt;
    private Long processedAt;
    private int delayMillis;
    private int delayDeviation;

    @JsonCreator
    public ClientMessage(@JsonProperty("id") int id) {
        this.id = id;
        this.createdAt = System.currentTimeMillis();
    }

    public int getId() {
        return id;
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

    public String metrics() {
        return new StringBuilder(64)
                .append("Message[id=")
                .append(id)
                .append(", receivedIn: ")
                .append(receivedAt - createdAt)
                .append(", processedIn: ")
                .append(processedAt - createdAt)
                .append("]")
                .toString();
    }

}
