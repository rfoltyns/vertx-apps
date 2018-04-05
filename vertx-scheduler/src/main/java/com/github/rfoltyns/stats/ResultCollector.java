package com.github.rfoltyns.stats;

import com.github.rfoltyns.vertx.ClientMessage;
import io.vertx.core.json.Json;
import org.apache.commons.math.stat.descriptive.rank.Percentile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.message.SimpleMessage;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class ResultCollector {

    private final Logger console = LogManager.getLogger(ResultCollector.class);

    public static final int PRECISION = 5;
    private final double[] percentiles;

    private final AtomicReference<Queue<ClientMessage>> resultsReference =
            new AtomicReference<>(new ConcurrentLinkedQueue<>());
    private final AtomicReference<Collection<Percentiles>> snapshotsReference =
            new AtomicReference<>(new ConcurrentLinkedQueue<>());

    private final Collection<WeightedPercentile> processedPercentiles = new ConcurrentLinkedQueue<>();
    private final Collection<WeightedPercentile> receivedPercentiles = new ConcurrentLinkedQueue<>();
    private final Collection<WeightedPercentile> servicedPercentiles = new ConcurrentLinkedQueue<>();

    private Timer timer = new Timer();

    private int requestsPerSecond;
    private final AtomicInteger numberOfCollectedSamples = new AtomicInteger();

    private Collection<StatsListener> snapshotListeners = new ArrayList<>();
    private Collection<StatsListener> summaryListeners = new ArrayList<>();
    private Logger samples = LogManager.getLogger("samples", new MessageFactory() {
        @Override
        public Message newMessage(Object message) {
            try {
                return new SimpleMessage(Json.mapper.writeValueAsString(message));
            } catch (Exception e) {
                console.error(e.getMessage());
            }
            return null;
        }

        @Override
        public Message newMessage(String message) {
            try {
                return new SimpleMessage(message);
            } catch (Exception e) {
                console.error(e.getMessage());
            }
            return null;
        }

        @Override
        public Message newMessage(String message, Object... params) {
            try {
                return new SimpleMessage(Json.mapper.writeValueAsString(params[0]));
            } catch (Exception e) {
                console.error(e.getMessage());
            }
            return null;
        }
    });

    public ResultCollector(double[] percentiles) {
        this.percentiles = percentiles;
        init(processedPercentiles, percentiles);
        init(receivedPercentiles, percentiles);
        init(servicedPercentiles, percentiles);
    }

    public void add(ClientMessage result) {
        samples.info(result);
        resultsReference.get().add(result);
    }

    public void setRequestsPerSecond(int requestsPerSecond) {
        this.requestsPerSecond = requestsPerSecond;
    }

    private void init(Collection<WeightedPercentile> processedPercentiles, double[] percentiles) {
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

                    Percentiles snapshot = new Percentiles(requestsPerSecond, resultList.size(), processedAveragePercentiles, receivedAveragePercentiles, serviceTimeAveragePercentiles);
                    snapshotsReference.get().add(snapshot);
                    numberOfCollectedSamples.addAndGet(resultList.size());

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
                    console.info("Requests per second: {}", requestsPerSecond);
                    console.info("Percentiles[10, 20, 30, 40, 50, 60, 70, 80, 90, 95, 99, 99.9, 99.99, 99.999]");

                    StringBuilder sb = new StringBuilder(512);
                    sb.append("\nResponse time;");

                    Iterator<WeightedPercentile> procPercentileIterator = processedPercentiles.iterator();
                    for (int ii = 0; ii < percentiles.length; ii++) {
                        WeightedPercentile weightedPercentile = procPercentileIterator.next();
                        for (Percentiles snapshot : latestSnapshots) {
                            weightedPercentile.add(snapshot.numberOfSamples, snapshot.processedAveragePercentiles[ii]);
                        }
                        sb.append(toPrecision(PRECISION, weightedPercentile.value())).append(";");
                    }

                    sb.append("\nAvg. time to client;");
                    Iterator<WeightedPercentile> rcvPercentileIterator = receivedPercentiles.iterator();
                    for (int ii = 0; ii < percentiles.length; ii++) {
                        WeightedPercentile weightedPercentile = rcvPercentileIterator.next();
                        for (Percentiles snapshot : latestSnapshots) {
                            weightedPercentile.add(snapshot.numberOfSamples, snapshot.receivedAveragePercentiles[ii]);
                        }
                        sb.append(toPrecision(PRECISION, weightedPercentile.value())).append(";");
                    }

                    sb.append("\nAvg. service time;");
                    Iterator<WeightedPercentile> svcPercentileIterator = servicedPercentiles.iterator();
                    for (int ii = 0; ii < percentiles.length; ii++) {
                        WeightedPercentile weightedPercentile = svcPercentileIterator.next();
                        for (Percentiles snapshot : latestSnapshots) {
                            weightedPercentile.add(snapshot.numberOfSamples, snapshot.serviceTimeAveragePercentiles[ii]);
                        }
                        sb.append(toPrecision(PRECISION, weightedPercentile.value())).append(";");
                    }

                    notifySummaryListeners(new Percentiles(requestsPerSecond, numberOfCollectedSamples.get(),
                            toArray(processedPercentiles),
                            toArray(receivedPercentiles),
                            toArray(servicedPercentiles)));
                    console.info(sb.toString());
                    console.info("Samples: {}", numberOfCollectedSamples);

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

    private double[] toArray(Collection<WeightedPercentile> percentileList) {
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

    public void removeSnapshotListenerByName(String listenerName) {
        snapshotListeners.removeIf(listener -> listenerName.equals(listener.getTarget()));
    }

    public void removeSummaryListenerByName(String listenerName) {
        summaryListeners.removeIf(listener -> listenerName.equals(listener.getTarget()));
    }

    public void reset() {
        console.info("Resetting stats");
        numberOfCollectedSamples.set(0);
        processedPercentiles.forEach(weightedPercentile -> reset(weightedPercentile));
        servicedPercentiles.forEach(weightedPercentile -> reset(weightedPercentile));
        receivedPercentiles.forEach(weightedPercentile -> reset(weightedPercentile));
    }

    private void reset(WeightedPercentile weightedPercentile) {
        weightedPercentile.setValue(0);
        weightedPercentile.setNumberOfSamples(0);
    }
}
