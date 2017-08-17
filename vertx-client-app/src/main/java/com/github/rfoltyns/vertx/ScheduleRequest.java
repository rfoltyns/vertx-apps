package com.github.rfoltyns.vertx;

public class ScheduleRequest {

    private int numberOfRequests;
    private int delayInMillis;
    private String consumer;
    private int numberOfThreads;
    private int delayDeviation;

    public int getNumberOfRequests() {
        return numberOfRequests;
    }

    public void setNumberOfRequests(int numberOfRequests) {
        this.numberOfRequests = numberOfRequests;
    }

    public int getDelayInMillis() {
        return delayInMillis;
    }

    public void setDelayInMillis(int delayInMillis) {
        this.delayInMillis = delayInMillis;
    }

    public String getConsumer() {
        return consumer;
    }

    public void setConsumer(String consumer) {
        this.consumer = consumer;
    }

    @Override
    public String toString() {
        return "ScheduleRequest{" +
                "numberOfRequests=" + numberOfRequests +
                ", delayInMillis=" + delayInMillis +
                '}';
    }

    public int getNumberOfThreads() {
        return numberOfThreads;
    }

    public void setNumberOfThreads(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
    }

    public int getDelayDeviation() {
        return delayDeviation;
    }

    public void setDelayDeviation(int delayDeviation) {
        this.delayDeviation = delayDeviation;
    }
}
