package com.github.rfoltyns.stats;

import org.junit.Assert;
import org.junit.Test;

public class WeightedPercentileUTest {

    @Test
    public void hasPercentile() {

        // given
        double percentile = 99.999;

        // when
        WeightedPercentile weightedPercentile = new WeightedPercentile(percentile);

        // then
        Assert.assertTrue(weightedPercentile.getPercentile() == percentile);
    }

    @Test
    public void hasNumberOfSamples() {

        // given
        int numberOfSamples = 5;

        double percentile = 99.999;
        WeightedPercentile weightedPercentile = new WeightedPercentile(percentile);

        // when
        weightedPercentile.add(numberOfSamples, 0);

        // then
        Assert.assertTrue(weightedPercentile.getNumberOfSamples() == numberOfSamples);
    }

    @Test
    public void addsNumberOfSamples() {

        // given
        int numberOfInitialSamples = 5;

        double percentile = 99.999;
        WeightedPercentile weightedPercentile = new WeightedPercentile(percentile);
        weightedPercentile.add(numberOfInitialSamples, 0);

        int numberOfNewSamples = 15;

        // when
        weightedPercentile.add(numberOfNewSamples, 0);

        // then
        Assert.assertTrue(weightedPercentile.getNumberOfSamples() == (numberOfInitialSamples + numberOfNewSamples));
    }


    @Test
    public void calculatesValueWrtActualWeight() {

        // given
        int numberOfInitialSamples = 5;

        double percentile = 99.999;
        WeightedPercentile weightedPercentile = new WeightedPercentile(percentile);
        weightedPercentile.add(numberOfInitialSamples, 10);

        int numberOfNewSamples = 10;

        // when
        weightedPercentile.add(numberOfNewSamples, 25);

        // then
        Assert.assertTrue(weightedPercentile.value() == 20);
    }

}
