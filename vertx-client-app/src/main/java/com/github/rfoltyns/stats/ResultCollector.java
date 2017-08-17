package com.github.rfoltyns.stats;

import com.github.rfoltyns.vertx.ClientMessage;
import org.apache.commons.math.stat.descriptive.rank.Percentile;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;

public class ResultCollector {

    public static final int PRECISION = 3;
    private final double[] percentiles;

    private final AtomicReference<Queue<ClientMessage>> resultsReference =
            new AtomicReference<>(new ConcurrentLinkedQueue<>());
    private final AtomicReference<Collection<Percentiles>> snapshotsReference =
            new AtomicReference<>(new ConcurrentLinkedQueue<>());

    private final List<WeightedPercentile> processedPercentiles = new ArrayList<>();
    private final List<WeightedPercentile> receivedPercentiles = new ArrayList<>();
    private final List<WeightedPercentile> servicedPercentiles = new ArrayList<>();

    private Timer timer = new Timer();

    private int requestsPerSecond;
    private int numberOfCollectedSamples;

    private List<StatsListener> snapshotListeners = new ArrayList<>();
    private List<StatsListener> summaryListeners = new ArrayList<>();

    public ResultCollector(double[] percentiles) {
        this.percentiles = percentiles;
        init(processedPercentiles, percentiles);
        init(receivedPercentiles, percentiles);
        init(servicedPercentiles, percentiles);
    }

    public void add(ClientMessage result) {
        resultsReference.get().add(result);
    }

    public void setRequestsPerSecond(int requestsPerSecond) {
        this.requestsPerSecond = requestsPerSecond;
    }

    private void init(List<WeightedPercentile> processedPercentiles, double[] percentiles) {
        for (double percentile : percentiles) {
            processedPercentiles.add(new WeightedPercentile(percentile));
        }
    }

    private Collection<ClientMessage> getLatestResults() {
        return resultsReference.getAndSet(new ConcurrentLinkedQueue<>());
    }

    private Collection<Percentiles> getLatestSnapshots() {
        return snapshotsReference.getAndSet(new ConcurrentLinkedQueue<>());
    }

    private TimerTask calculateStats() {
        return new TimerTask() {
            @Override
            public void run() {
                Collection<ClientMessage> resultList = getLatestResults();
                if (resultList.size() > 0) {

                    double[] receivedAveragePercentiles = new double[percentiles.length];
                    double[] processedAveragePercentiles = new double[percentiles.length];
                    double[] serviceTimeAveragePercentiles = new double[percentiles.length];

                    for (int ii = 0; ii < percentiles.length; ii++) {
                        receivedAveragePercentiles[ii] = getPercentiles(percentiles[ii], getReceivedAvg(resultList));
                        processedAveragePercentiles[ii] = getPercentiles(percentiles[ii], getProcessedAvg(resultList));
                        serviceTimeAveragePercentiles[ii] = getPercentiles(percentiles[ii], getServiceTime(0, resultList.size(), resultList));
                    }

                    Percentiles snapshot = new Percentiles(resultList.size(), processedAveragePercentiles, receivedAveragePercentiles, serviceTimeAveragePercentiles);
                    snapshotsReference.get().add(snapshot);
                    numberOfCollectedSamples += resultList.size();

                    notifySnapshotListeners(snapshot);
                }
            }
        };
    }

    private TimerTask printStats() {
        return new TimerTask() {
            @Override
            public void run() {
                Collection<Percentiles> latestSnapshots = getLatestSnapshots();
                if (latestSnapshots.size() > 0) {
                    System.out.println("Requests per second: " + requestsPerSecond);
                    System.out.println("Percentiles[10, 20, 30, 40, 50, 60, 70, 80, 90, 95, 99, 99.9, 99.99, 99.999]");

                    StringBuilder sb = new StringBuilder("\nResponse time;");

                    for (int ii = 0; ii < percentiles.length; ii++) {
                        WeightedPercentile weightedPercentile = processedPercentiles.get(ii);
                        for (Percentiles snapshot : latestSnapshots) {
                            weightedPercentile.add(snapshot.numberOfSamples, snapshot.processedAveragePercentiles[ii]);
                        }
                        sb.append(toPrecision(PRECISION, weightedPercentile.value())).append(";");
                    }

                    sb.append("\nAvg. time to client;");
                    for (int ii = 0; ii < percentiles.length; ii++) {
                        WeightedPercentile weightedPercentile = receivedPercentiles.get(ii);
                        for (Percentiles snapshot : latestSnapshots) {
                            weightedPercentile.add(snapshot.numberOfSamples, snapshot.receivedAveragePercentiles[ii]);
                        }
                        sb.append(toPrecision(PRECISION, weightedPercentile.value())).append(";");
                    }

                    sb.append("\nAvg. service time;");
                    for (int ii = 0; ii < percentiles.length; ii++) {
                        WeightedPercentile weightedPercentile = servicedPercentiles.get(ii);
                        for (Percentiles snapshot : latestSnapshots) {
                            weightedPercentile.add(snapshot.numberOfSamples, snapshot.serviceTimeAveragePercentiles[ii]);
                        }
                        sb.append(toPrecision(PRECISION, weightedPercentile.value())).append(";");
                    }

                    notifySummaryListeners(new Percentiles(numberOfCollectedSamples,
                            toArray(processedPercentiles),
                            toArray(receivedPercentiles),
                            toArray(servicedPercentiles)));
                    System.out.println(sb.toString());
                    System.out.println("Samples: " + numberOfCollectedSamples);

                }
            }
        };
    }

    private void notifySnapshotListeners(Percentiles snapshot) {
        for (StatsListener listener : snapshotListeners) {
            try {
                listener.notify(snapshot);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }

    private void notifySummaryListeners(Percentiles percentiles) {
        summaryListeners.stream().forEach(statsListener -> {
            try {
                statsListener.notify(percentiles);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        });
    }

    private double[] toArray(List<WeightedPercentile> percentileList) {
        return percentileList.stream()
                .mapToDouble(weightedPercentile -> weightedPercentile.getValue())
                .toArray();
    }

    public ResultCollector refreshEachMillis(int calculateStatsInterval) {
        timer.scheduleAtFixedRate(calculateStats(), 1000, calculateStatsInterval);
        return this;
    }

    public ResultCollector printEachMillis(int printStatsInterval) {
        timer.scheduleAtFixedRate(printStats(), 1000, printStatsInterval);
        return this;
    }

    private double getPercentiles(double percentile, double[] values) {
        return toPrecision(PRECISION * 2, new Percentile().evaluate(values, percentile));
    }

    private double[] getReceivedAvg(Collection<ClientMessage> resultList) {
        double[] values = new double[resultList.size()];
        int index = 0;
        for (ClientMessage message : resultList) {
            values[index++] = (message.getReceivedAt() - message.getCreatedAt());
        }
        return values;
    }

    private double[] getProcessedAvg(Collection<ClientMessage> resultList) {
        double[] values = new double[resultList.size()];
        int index = 0;
        for (ClientMessage message : resultList) {
            values[index++] = (message.getProcessedAt() - message.getCreatedAt());
        }
        return values;
    }

    private double[] getServiceTime(int begin, int end, Collection<ClientMessage> resultList) {
        int index = 0;
        double[] values = new double[resultList.size()];
        for (ClientMessage message : resultList) {
            values[index++] = (message.getProcessedAt() - message.getReceivedAt());
        }
        return values;
    }

    private double toPrecision(int precision, double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(precision, RoundingMode.HALF_EVEN);
        return bd.doubleValue();
    }

    public void addSnapshotListener(StatsListener socketPublisher) {
        snapshotListeners.add(socketPublisher);
    }

    public void addSummaryListener(StatsListener socketPublisher) {
        summaryListeners.add(socketPublisher);
    }
}
