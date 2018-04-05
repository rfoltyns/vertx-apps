package com.github.rfoltyns.grpc.metrics;

import io.opencensus.contrib.grpc.metrics.RpcViewConstants;
import io.opencensus.stats.Stats;
import io.opencensus.stats.View;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VertxGrpcMetrics {

    private static final Set<View> REGISTERED_VIEWS = new HashSet<>();

    private static final List<View> CLIENT_VIEWS = new ArrayList<>();
    static {
        CLIENT_VIEWS.add(RpcViewConstants.RPC_CLIENT_ERROR_COUNT_HOUR_VIEW);
        CLIENT_VIEWS.add(RpcViewConstants.RPC_CLIENT_ERROR_COUNT_MINUTE_VIEW);
        CLIENT_VIEWS.add(RpcViewConstants.RPC_CLIENT_ERROR_COUNT_VIEW);
        CLIENT_VIEWS.add(RpcViewConstants.RPC_CLIENT_FINISHED_COUNT_CUMULATIVE_VIEW);
        CLIENT_VIEWS.add(RpcViewConstants.RPC_CLIENT_FINISHED_COUNT_HOUR_VIEW);
        CLIENT_VIEWS.add(RpcViewConstants.RPC_CLIENT_FINISHED_COUNT_MINUTE_VIEW);
        CLIENT_VIEWS.add(RpcViewConstants.RPC_CLIENT_REQUEST_BYTES_HOUR_VIEW);
        CLIENT_VIEWS.add(RpcViewConstants.RPC_CLIENT_REQUEST_BYTES_MINUTE_VIEW);
        CLIENT_VIEWS.add(RpcViewConstants.RPC_CLIENT_REQUEST_BYTES_VIEW);
        CLIENT_VIEWS.add(RpcViewConstants.RPC_CLIENT_REQUEST_COUNT_HOUR_VIEW);
        CLIENT_VIEWS.add(RpcViewConstants.RPC_CLIENT_REQUEST_COUNT_MINUTE_VIEW);
        CLIENT_VIEWS.add(RpcViewConstants.RPC_CLIENT_REQUEST_COUNT_VIEW);
        CLIENT_VIEWS.add(RpcViewConstants.RPC_CLIENT_RESPONSE_BYTES_HOUR_VIEW);
        CLIENT_VIEWS.add(RpcViewConstants.RPC_CLIENT_RESPONSE_BYTES_MINUTE_VIEW);
        CLIENT_VIEWS.add(RpcViewConstants.RPC_CLIENT_RESPONSE_BYTES_VIEW);
        CLIENT_VIEWS.add(RpcViewConstants.RPC_CLIENT_RESPONSE_COUNT_HOUR_VIEW);
        CLIENT_VIEWS.add(RpcViewConstants.RPC_CLIENT_RESPONSE_COUNT_MINUTE_VIEW);
        CLIENT_VIEWS.add(RpcViewConstants.RPC_CLIENT_RESPONSE_COUNT_VIEW);
        CLIENT_VIEWS.add(RpcViewConstants.RPC_CLIENT_ROUNDTRIP_LATENCY_HOUR_VIEW);
        CLIENT_VIEWS.add(RpcViewConstants.RPC_CLIENT_ROUNDTRIP_LATENCY_MINUTE_VIEW);
        CLIENT_VIEWS.add(RpcViewConstants.RPC_CLIENT_ROUNDTRIP_LATENCY_VIEW);
        CLIENT_VIEWS.add(RpcViewConstants.RPC_CLIENT_SERVER_ELAPSED_TIME_HOUR_VIEW);
        CLIENT_VIEWS.add(RpcViewConstants.RPC_CLIENT_SERVER_ELAPSED_TIME_MINUTE_VIEW);
        CLIENT_VIEWS.add(RpcViewConstants.RPC_CLIENT_SERVER_ELAPSED_TIME_VIEW);
        CLIENT_VIEWS.add(RpcViewConstants.RPC_CLIENT_STARTED_COUNT_CUMULATIVE_VIEW);
        CLIENT_VIEWS.add(RpcViewConstants.RPC_CLIENT_STARTED_COUNT_HOUR_VIEW);
        CLIENT_VIEWS.add(RpcViewConstants.RPC_CLIENT_STARTED_COUNT_MINUTE_VIEW);
        CLIENT_VIEWS.add(RpcViewConstants.RPC_CLIENT_UNCOMPRESSED_REQUEST_BYTES_HOUR_VIEW);
        CLIENT_VIEWS.add(RpcViewConstants.RPC_CLIENT_UNCOMPRESSED_REQUEST_BYTES_MINUTE_VIEW);
        CLIENT_VIEWS.add(RpcViewConstants.RPC_CLIENT_UNCOMPRESSED_REQUEST_BYTES_VIEW);
        CLIENT_VIEWS.add(RpcViewConstants.RPC_CLIENT_UNCOMPRESSED_RESPONSE_BYTES_HOUR_VIEW);
        CLIENT_VIEWS.add(RpcViewConstants.RPC_CLIENT_UNCOMPRESSED_RESPONSE_BYTES_MINUTE_VIEW);
        CLIENT_VIEWS.add(RpcViewConstants.RPC_CLIENT_UNCOMPRESSED_RESPONSE_BYTES_VIEW);
    }

    private static final List<View> SERVER_VIEWS = new ArrayList<>();
    static {
        SERVER_VIEWS.add(RpcViewConstants.RPC_SERVER_ERROR_COUNT_MINUTE_VIEW);
        SERVER_VIEWS.add(RpcViewConstants.RPC_SERVER_SERVER_LATENCY_MINUTE_VIEW);
        SERVER_VIEWS.add(RpcViewConstants.RPC_SERVER_SERVER_ELAPSED_TIME_MINUTE_VIEW);
        SERVER_VIEWS.add(RpcViewConstants.RPC_SERVER_REQUEST_BYTES_MINUTE_VIEW);
        SERVER_VIEWS.add(RpcViewConstants.RPC_SERVER_RESPONSE_BYTES_MINUTE_VIEW);
        SERVER_VIEWS.add(RpcViewConstants.RPC_SERVER_REQUEST_COUNT_MINUTE_VIEW);
        SERVER_VIEWS.add(RpcViewConstants.RPC_SERVER_RESPONSE_COUNT_MINUTE_VIEW);
        SERVER_VIEWS.add(RpcViewConstants.RPC_SERVER_UNCOMPRESSED_REQUEST_BYTES_MINUTE_VIEW);
        SERVER_VIEWS.add(RpcViewConstants.RPC_SERVER_UNCOMPRESSED_RESPONSE_BYTES_MINUTE_VIEW);
        SERVER_VIEWS.add(RpcViewConstants.RPC_SERVER_STARTED_COUNT_MINUTE_VIEW);
        SERVER_VIEWS.add(RpcViewConstants.RPC_SERVER_FINISHED_COUNT_MINUTE_VIEW);
        SERVER_VIEWS.add(RpcViewConstants.RPC_SERVER_ERROR_COUNT_HOUR_VIEW);
        SERVER_VIEWS.add(RpcViewConstants.RPC_SERVER_SERVER_LATENCY_HOUR_VIEW);
        SERVER_VIEWS.add(RpcViewConstants.RPC_SERVER_SERVER_ELAPSED_TIME_HOUR_VIEW);
        SERVER_VIEWS.add(RpcViewConstants.RPC_SERVER_REQUEST_BYTES_HOUR_VIEW);
        SERVER_VIEWS.add(RpcViewConstants.RPC_SERVER_RESPONSE_BYTES_HOUR_VIEW);
        SERVER_VIEWS.add(RpcViewConstants.RPC_SERVER_REQUEST_COUNT_HOUR_VIEW);
        SERVER_VIEWS.add(RpcViewConstants.RPC_SERVER_RESPONSE_COUNT_HOUR_VIEW);
        SERVER_VIEWS.add(RpcViewConstants.RPC_SERVER_UNCOMPRESSED_REQUEST_BYTES_HOUR_VIEW);
        SERVER_VIEWS.add(RpcViewConstants.RPC_SERVER_UNCOMPRESSED_RESPONSE_BYTES_HOUR_VIEW);
        SERVER_VIEWS.add(RpcViewConstants.RPC_SERVER_STARTED_COUNT_HOUR_VIEW);
        SERVER_VIEWS.add(RpcViewConstants.RPC_SERVER_FINISHED_COUNT_HOUR_VIEW);
        SERVER_VIEWS.add(RpcViewConstants.RPC_SERVER_ERROR_COUNT_VIEW);
        SERVER_VIEWS.add(RpcViewConstants.RPC_SERVER_SERVER_LATENCY_VIEW);
        SERVER_VIEWS.add(RpcViewConstants.RPC_SERVER_SERVER_ELAPSED_TIME_VIEW);
        SERVER_VIEWS.add(RpcViewConstants.RPC_SERVER_REQUEST_BYTES_VIEW);
        SERVER_VIEWS.add(RpcViewConstants.RPC_SERVER_RESPONSE_BYTES_VIEW);
        SERVER_VIEWS.add(RpcViewConstants.RPC_SERVER_REQUEST_COUNT_VIEW);
        SERVER_VIEWS.add(RpcViewConstants.RPC_SERVER_RESPONSE_COUNT_VIEW);
        SERVER_VIEWS.add(RpcViewConstants.RPC_SERVER_UNCOMPRESSED_REQUEST_BYTES_VIEW);
        SERVER_VIEWS.add(RpcViewConstants.RPC_SERVER_UNCOMPRESSED_RESPONSE_BYTES_VIEW);
        SERVER_VIEWS.add(RpcViewConstants.RPC_SERVER_STARTED_COUNT_CUMULATIVE_VIEW);
        SERVER_VIEWS.add(RpcViewConstants.RPC_SERVER_FINISHED_COUNT_CUMULATIVE_VIEW);
    }

    private static final List<View> CLIENT_CUMULATIVE_VIEWS = new ArrayList<>();
    static {
        CLIENT_CUMULATIVE_VIEWS.add(RpcViewConstants.RPC_CLIENT_ERROR_COUNT_VIEW);
        CLIENT_CUMULATIVE_VIEWS.add(RpcViewConstants.RPC_CLIENT_FINISHED_COUNT_CUMULATIVE_VIEW);
        CLIENT_CUMULATIVE_VIEWS.add(RpcViewConstants.RPC_CLIENT_REQUEST_BYTES_VIEW);
        CLIENT_CUMULATIVE_VIEWS.add(RpcViewConstants.RPC_CLIENT_REQUEST_COUNT_VIEW);
        CLIENT_CUMULATIVE_VIEWS.add(RpcViewConstants.RPC_CLIENT_RESPONSE_BYTES_VIEW);
        CLIENT_CUMULATIVE_VIEWS.add(RpcViewConstants.RPC_CLIENT_RESPONSE_COUNT_VIEW);
        CLIENT_CUMULATIVE_VIEWS.add(RpcViewConstants.RPC_CLIENT_ROUNDTRIP_LATENCY_VIEW);
        CLIENT_CUMULATIVE_VIEWS.add(RpcViewConstants.RPC_CLIENT_SERVER_ELAPSED_TIME_VIEW);
        CLIENT_CUMULATIVE_VIEWS.add(RpcViewConstants.RPC_CLIENT_STARTED_COUNT_CUMULATIVE_VIEW);
        CLIENT_CUMULATIVE_VIEWS.add(RpcViewConstants.RPC_CLIENT_UNCOMPRESSED_REQUEST_BYTES_VIEW);
        CLIENT_CUMULATIVE_VIEWS.add(RpcViewConstants.RPC_CLIENT_UNCOMPRESSED_RESPONSE_BYTES_VIEW);
    }

    private static final List<View> CLIENT_MINUTE_VIEWS = new ArrayList<>();
    static {
        CLIENT_MINUTE_VIEWS.add(RpcViewConstants.RPC_CLIENT_ERROR_COUNT_MINUTE_VIEW);
        CLIENT_MINUTE_VIEWS.add(RpcViewConstants.RPC_CLIENT_FINISHED_COUNT_MINUTE_VIEW);
        CLIENT_MINUTE_VIEWS.add(RpcViewConstants.RPC_CLIENT_REQUEST_BYTES_MINUTE_VIEW);
        CLIENT_MINUTE_VIEWS.add(RpcViewConstants.RPC_CLIENT_REQUEST_COUNT_MINUTE_VIEW);
        CLIENT_MINUTE_VIEWS.add(RpcViewConstants.RPC_CLIENT_RESPONSE_BYTES_MINUTE_VIEW);
        CLIENT_MINUTE_VIEWS.add(RpcViewConstants.RPC_CLIENT_RESPONSE_COUNT_MINUTE_VIEW);
        CLIENT_MINUTE_VIEWS.add(RpcViewConstants.RPC_CLIENT_ROUNDTRIP_LATENCY_MINUTE_VIEW);
        CLIENT_MINUTE_VIEWS.add(RpcViewConstants.RPC_CLIENT_SERVER_ELAPSED_TIME_MINUTE_VIEW);
        CLIENT_MINUTE_VIEWS.add(RpcViewConstants.RPC_CLIENT_STARTED_COUNT_MINUTE_VIEW);
        CLIENT_MINUTE_VIEWS.add(RpcViewConstants.RPC_CLIENT_UNCOMPRESSED_REQUEST_BYTES_MINUTE_VIEW);
        CLIENT_MINUTE_VIEWS.add(RpcViewConstants.RPC_CLIENT_UNCOMPRESSED_RESPONSE_BYTES_MINUTE_VIEW);
    }

    private static final List<View> CLIENT_HOUR_VIEWS = new ArrayList<>();
    static {
        CLIENT_HOUR_VIEWS.add(RpcViewConstants.RPC_CLIENT_ERROR_COUNT_HOUR_VIEW);
        CLIENT_HOUR_VIEWS.add(RpcViewConstants.RPC_CLIENT_FINISHED_COUNT_HOUR_VIEW);
        CLIENT_HOUR_VIEWS.add(RpcViewConstants.RPC_CLIENT_REQUEST_BYTES_HOUR_VIEW);
        CLIENT_HOUR_VIEWS.add(RpcViewConstants.RPC_CLIENT_REQUEST_COUNT_HOUR_VIEW);
        CLIENT_HOUR_VIEWS.add(RpcViewConstants.RPC_CLIENT_RESPONSE_BYTES_HOUR_VIEW);
        CLIENT_HOUR_VIEWS.add(RpcViewConstants.RPC_CLIENT_RESPONSE_COUNT_HOUR_VIEW);
        CLIENT_HOUR_VIEWS.add(RpcViewConstants.RPC_CLIENT_ROUNDTRIP_LATENCY_HOUR_VIEW);
        CLIENT_HOUR_VIEWS.add(RpcViewConstants.RPC_CLIENT_SERVER_ELAPSED_TIME_HOUR_VIEW);
        CLIENT_HOUR_VIEWS.add(RpcViewConstants.RPC_CLIENT_STARTED_COUNT_HOUR_VIEW);
        CLIENT_HOUR_VIEWS.add(RpcViewConstants.RPC_CLIENT_UNCOMPRESSED_REQUEST_BYTES_HOUR_VIEW);
        CLIENT_HOUR_VIEWS.add(RpcViewConstants.RPC_CLIENT_UNCOMPRESSED_RESPONSE_BYTES_HOUR_VIEW);
    }

    public static void registerAllClientViews() {
        for (View view : CLIENT_VIEWS) {
            Stats.getViewManager().registerView(view);
            REGISTERED_VIEWS.add(view);
        }
    }

    public static void registerAllClientCumulativeViews() {
        for (View view : CLIENT_CUMULATIVE_VIEWS) {
            Stats.getViewManager().registerView(view);
            REGISTERED_VIEWS.add(view);
        }
    }

    public static void registerAllClientHourViews() {
        for (View view : CLIENT_HOUR_VIEWS) {
            Stats.getViewManager().registerView(view);
            REGISTERED_VIEWS.add(view);
        }
    }

    public static void registerAllClientMinuteViews() {
        for (View view : CLIENT_MINUTE_VIEWS) {
            Stats.getViewManager().registerView(view);
            REGISTERED_VIEWS.add(view);
        }
    }

    public static Iterable<? extends View> getAllClientViews() {
        return CLIENT_VIEWS;
    }

    public static Iterable<? extends View> getAllRegisteredViews() {
        return REGISTERED_VIEWS;
    }

    public enum Metric {
        ERROR_COUNT,
        FINISHED_COUNT,
        REQUEST_BYTES,
        REQUEST_COUNT,
        RESPONSE_BYTES,
        RESPONSE_COUNT,
        LATENCY,
        ELAPSED_TIME,
        STARTED_COUNT,
        UNCOMPRESSED_REQUEST_BYTES,
        UNCOMPRESSED_RESPONSE_BYTES
    }

    public enum Span {
        HOUR,
        MINUTE,
        CUMULATIVE
    }

    public static void registerServerView(Metric metric, Span span) {
        for (View view : SERVER_VIEWS) {
            if (view.getName().asString().contains(metric.name().toLowerCase())
                    && view.getName().toString().contains(span.name().toLowerCase())) {
                Stats.getViewManager().registerView(view);
                REGISTERED_VIEWS.add(view);
            }
        }
    }
}
