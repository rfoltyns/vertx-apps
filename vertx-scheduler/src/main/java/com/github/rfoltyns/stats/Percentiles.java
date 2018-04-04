package com.github.rfoltyns.stats;

public class Percentiles {

    public final int requestsPerSecond;
    public final int numberOfSamples;
    public final double[] processedAveragePercentiles;
    public final double[] receivedAveragePercentiles;
    public final double[] serviceTimeAveragePercentiles;

    public Percentiles(
            int requestsPerSecond,
            int numberOfSamples,
            double[] processedAveragePercentiles,
            double[] receivedAveragePercentiles,
            double[] serviceTimeAveragePercentiles) {
        this.requestsPerSecond = requestsPerSecond;
        this.numberOfSamples = numberOfSamples;
        this.processedAveragePercentiles = processedAveragePercentiles;
        this.receivedAveragePercentiles = receivedAveragePercentiles;
        this.serviceTimeAveragePercentiles = serviceTimeAveragePercentiles;
    }

}
