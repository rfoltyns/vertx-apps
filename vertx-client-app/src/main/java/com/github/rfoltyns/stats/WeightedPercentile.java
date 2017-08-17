package com.github.rfoltyns.stats;

public class WeightedPercentile {

    private int numberOfSamples;
    private final double percentile;
    private double value;

    public WeightedPercentile(double percentile) {
        this.percentile = percentile;
    }

    public void add(int numberOfSamples, double value) {
        double currentWeight = this.numberOfSamples * this.value;
        double addedWeight = numberOfSamples * value;
        this.value = (currentWeight + addedWeight) / (this.numberOfSamples += numberOfSamples);
    }

    public double getPercentile() {
        return percentile;
    }

    public int getNumberOfSamples() {
        return numberOfSamples;
    }

    public void setNumberOfSamples(int numberOfSamples) {
        this.numberOfSamples = numberOfSamples;
    }

    public double getValue() {
        return value();
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double value() {
        return value;
    }
}
