package com.github.rfoltyns.vertx;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.rfoltyns.grpc.metrics.VertxGrpcMetrics;
import com.github.rfoltyns.vertx.grpc.MeasureReply;
import com.github.rfoltyns.vertx.grpc.MeasureRequest;
import com.github.rfoltyns.vertx.grpc.ReceiverGrpc;
import com.google.protobuf.ByteString;
import io.grpc.LoadBalancer;
import io.grpc.ManagedChannel;
import io.grpc.util.RoundRobinLoadBalancerFactory;
import io.opencensus.stats.Stats;
import io.opencensus.stats.View;
import io.opencensus.stats.ViewData;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import io.vertx.grpc.VertxChannelBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Queue;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class GrpcLoadClient extends AbstractVerticle {

    private final Logger console = LogManager.getLogger(HttpLoadClient.class);

    private ReceiverGrpc.ReceiverVertxStub vertxStub;

    private AtomicInteger counter = new AtomicInteger();

    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(500);
    private static Queue<ScheduledFuture<?>> scheduledFutures = new ConcurrentLinkedQueue<>();
    private static int numberOfRequestsPerThread = 100;
    private Random random = new Random();

    private static Timer timer = new Timer();
    private static TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            for (View exportedView : VertxGrpcMetrics.getAllRegisteredViews()) {
                ViewData exportedViewData = Stats.getViewManager().getView(exportedView.getName());
                try {
                    System.out.println(exportedViewData.getView().getName().asString() + ": " + Json.mapper.writeValueAsString(exportedViewData.getAggregationMap()));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    static {
        timer.scheduleAtFixedRate(timerTask,1000,1000);
    }

    @Override
    public void start() {

        ManagedChannel channel = VertxChannelBuilder
                // FIXME: dummy host passed just to create the builder; implement per target resolution in NameResolver(?)
                .forTarget(vertx, "localhost")
                .nameResolverFactory(new LocalhostNameResolver(vertx, "grpc-server", "ipaddresses"))
                .loadBalancerFactory(new LoadBalancer.Factory() {
                     @Override
                     public LoadBalancer newLoadBalancer(LoadBalancer.Helper helper) {
                         return RoundRobinLoadBalancerFactory.getInstance().newLoadBalancer(helper);
                     }
                })
                .usePlaintext(true)
                .build();

        // Get a stub to use for interacting with the remote service
        vertxStub = ReceiverGrpc.newVertxStub(channel);

        vertx.eventBus().consumer("grpcload", (Message<Buffer> message) -> {
            ScheduleRequest scheduleRequest =  JsonUtils.decodeValue(message.body(), ScheduleRequest.class);

            for (int ii = 0; ii < scheduleRequest.getNumberOfThreads(); ii++) {
                scheduleSendRequestTask(scheduleRequest);
            }

            if (scheduleRequest.getNumberOfThreads() < 0) {
                cancelRunningTasks(scheduleRequest);
            } else {
                console.info("Request scheduled: ", scheduleRequest);
            }
        });

        VertxGrpcMetrics.registerAllClientMinuteViews();

    }

    private void sendRequest(ScheduleRequest scheduleRequest) {

        MeasureRequest request = createMessage(scheduleRequest);

        // Call the remote service
        vertxStub.measure(request, ar -> {
            if (ar.succeeded()) {
                MeasureReply response = ar.result();

                ClientMessage clientMessage = new ClientMessage(response.getId());
                clientMessage.setCreatedAt(response.getCreatedAt());
                clientMessage.setReceivedAt(response.getReceivedAt());
                clientMessage.setProcessedAt(System.currentTimeMillis());

                Collector.CollectorHolder.INSTANCE.add(clientMessage);
            } else {
                System.out.println("Coult not reach server " + ar.cause().getMessage());
            }
        });

    }

    private void cancelRunningTasks(ScheduleRequest scheduleRequest) {
        for (int ii = 0; ii < Math.abs(scheduleRequest.getNumberOfThreads()); ii++) {
            if (scheduledFutures.size() > 0)
                scheduledFutures.poll().cancel(false);
        }
    }

    private void scheduleSendRequestTask(ScheduleRequest scheduleRequest) {
        Runnable runnable = () -> {
            for (int n = 0; n < numberOfRequestsPerThread; n++) {
                sendRequest(scheduleRequest);
                try {
                    Thread.currentThread().sleep(random.nextInt(19) + 1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        };

        ScheduledFuture<?> scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(
                runnable,
                1L,
                1L,
                TimeUnit.SECONDS);

        scheduledFutures.add(scheduledFuture);
    }

    private MeasureRequest createMessage(ScheduleRequest scheduleRequest) {
        byte[] data = new byte[scheduleRequest.getMessageSizeInBytes()];
        random.nextBytes(data);

        MeasureRequest measureRequest = MeasureRequest.newBuilder()
                .setId(counter.incrementAndGet())
                .setCreatedAt(System.currentTimeMillis())
                .setDelayMillis(scheduleRequest.getDelayInMillis())
                .setDelayDeviation(scheduleRequest.getDelayDeviation())
                .setConsumer(scheduleRequest.getConsumer() != null ? scheduleRequest.getConsumer() : "")
                .setNumberOfHops(scheduleRequest.getNumberOfHops())
                .setData(ByteString.copyFrom(data))
                .putSampleMap("sampleData", "someValue")
                .build();

        return measureRequest;
    }

}
