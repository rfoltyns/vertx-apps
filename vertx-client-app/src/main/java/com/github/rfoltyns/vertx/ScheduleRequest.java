package com.github.rfoltyns.vertx;

public class ScheduleRequest {

    private String type;
    private String consumer;
    private int numberOfRequests;
    private int numberOfThreads;
    private int requestsPerThread;
    private int messageSizeInBytes;
    private int delayInMillis;
    private int delayDeviation;
    private int numberOfHops;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNumberOfRequests() {
        return numberOfRequests;
    }

    public void setNumberOfRequests(int numberOfRequests) {
        this.numberOfRequests = numberOfRequests;
    }

    public String getConsumer() {
        return consumer;
    }

    public void setConsumer(String consumer) {
        this.consumer = consumer;
    }

    public int getNumberOfThreads() {
        return numberOfThreads;
    }

    public void setNumberOfThreads(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
    }

    public int getRequestsPerThread() {
        return requestsPerThread;
    }

    public void setRequestsPerThread(int requestsPerThread) {
        this.requestsPerThread = requestsPerThread;
    }

    public int getMessageSizeInBytes() {
        return messageSizeInBytes;
    }

    public void setMessageSizeInBytes(int messageSizeInBytes) {
        this.messageSizeInBytes = messageSizeInBytes;
    }

    public int getDelayInMillis() {
        return delayInMillis;
    }

    public void setDelayInMillis(int delayInMillis) {
        this.delayInMillis = delayInMillis;
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

    @Override
    public String toString() {
        return new StringBuilder(128)
                .append("ScheduleRequest {")
                .append("consumer='").append( consumer)
                .append(", numberOfRequests=").append(numberOfRequests)
                .append(", numberOfThreads=").append( numberOfThreads)
                .append("' requestsPerThread=").append(requestsPerThread)
                .append(", delayInMillis=").append( delayInMillis)
                .append(", delayDeviation=").append( delayDeviation)
                .append('}')
                .toString();
    }
}
