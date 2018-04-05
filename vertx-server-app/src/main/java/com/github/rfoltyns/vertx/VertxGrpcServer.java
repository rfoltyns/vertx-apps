package com.github.rfoltyns.vertx;

import com.github.rfoltyns.grpc.metrics.VertxGrpcMetrics;
import com.github.rfoltyns.vertx.grpc.MeasureReply;
import com.github.rfoltyns.vertx.grpc.MeasureRequest;
import com.github.rfoltyns.vertx.grpc.ReceiverGrpc;
import io.opencensus.stats.Stats;
import io.opencensus.stats.View;
import io.opencensus.stats.ViewData;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.grpc.VertxServer;
import io.vertx.grpc.VertxServerBuilder;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

public class VertxGrpcServer extends AbstractVerticle {

    private static AtomicInteger counter = new AtomicInteger();
    private static Timer timer = new Timer();
    private static TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            if (counter.get() > 0)
                System.out.println("Requests handled last second: " + counter.getAndSet(0));
                for (View exportedView : VertxGrpcMetrics.getAllRegisteredViews()) {
                    ViewData exportedViewData = Stats.getViewManager().getView(exportedView.getName());
                    System.out.println(exportedViewData.getView().getName().asString() + ": " + exportedViewData.getAggregationMap());

                }
        }
    };

    static {
        timer.scheduleAtFixedRate(timerTask,1000,1000);
    }

    @Override
    public void start() throws Exception {

        ReceiverGrpc.ReceiverVertxImplBase measureService = new ReceiverGrpc.ReceiverVertxImplBase() {
            @Override
            public void measure(MeasureRequest request, Future<MeasureReply> future) {
                counter.incrementAndGet();

                MeasureReply reply = MeasureReply.newBuilder()
                        .setId(request.getId())
                        .setCreatedAt(request.getCreatedAt())
                        .setReceivedAt(System.currentTimeMillis())
                        .build();

                future.complete(reply);
            }
        };

        VertxServer rpcServer = VertxServerBuilder
                .forAddress(vertx, "localhost", Integer.getInteger("grpc.server.port", 7070))
                .addService(measureService)
                .build();

        VertxGrpcMetrics.registerServerView(VertxGrpcMetrics.Metric.REQUEST_BYTES, VertxGrpcMetrics.Span.MINUTE);

        System.out.println("Is clustered: " + vertx.isClustered());

        // Start is asynchronous
        rpcServer.start();
    }

}
