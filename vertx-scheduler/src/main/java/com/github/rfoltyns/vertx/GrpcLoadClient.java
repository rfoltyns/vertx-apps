package com.github.rfoltyns.vertx;

import com.github.rfoltyns.vertx.grpc.MeasureReply;
import com.github.rfoltyns.vertx.grpc.MeasureRequest;
import com.github.rfoltyns.vertx.grpc.MeasureRequestOrBuilder;
import com.github.rfoltyns.vertx.grpc.ReceiverGrpc;
import com.google.protobuf.ByteString;
import examples.HelloReply;
import examples.HelloRequest;
import io.grpc.ManagedChannel;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.grpc.VertxChannelBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Queue;
import java.util.Random;
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

    private DeliveryOptions resultDeliverOptions = new DeliveryOptions()
            .setCodecName(CollectorMessageCodec.class.getSimpleName());

    @Override
    public void start() {

        ManagedChannel channel = VertxChannelBuilder
                .forAddress(vertx, "localhost", 7070)
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
//                System.out.println("Got the server response: " + ar.result().getId());
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
