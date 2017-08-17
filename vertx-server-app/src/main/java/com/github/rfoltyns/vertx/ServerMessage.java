package com.github.rfoltyns.vertx;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ServerMessage {

    private final int id;
    private long createdAt;
    private long receivedAt;
    private long processedAt;
    private int delayMillis;
    private int delayDeviation;

    @JsonCreator
    public ServerMessage(@JsonProperty("id") int id, @JsonProperty("createdAt") long createdAt) {
        this.id = id;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
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

    public void setProcessedAt(long processedAt) {
        this.processedAt = processedAt;
    }

    public void setReceivedAt(long receivedAt) {
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
//                .append(", processedIn: ")
//                .append(processedAt - createdAt)
                .append("]")
                .toString();
    }

}
