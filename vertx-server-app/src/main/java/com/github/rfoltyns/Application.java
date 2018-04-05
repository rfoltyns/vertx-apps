package com.github.rfoltyns;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import com.github.rfoltyns.vertx.Consumer;
import com.github.rfoltyns.vertx.JsonUtils;
import com.github.rfoltyns.vertx.Proxy;
import com.github.rfoltyns.vertx.VertxGrpcServer;
import com.github.rfoltyns.vertx.VertxHttpServer;
import com.hazelcast.core.IMap;
import com.hazelcast.nio.Address;
import io.vertx.core.AsyncResult;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.Json;
import io.vertx.core.shareddata.AsyncMap;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.ext.dropwizard.DropwizardMetricsOptions;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;

public class Application {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger("elasticsearch");

    public static void main(String... args) {
        System.out.println("Starting server");
        JsonUtils.mapper = new ObjectMapper(new CBORFactory()).registerModule(new AfterburnerModule());

        HazelcastClusterManager mgr = new HazelcastClusterManager();

        Vertx.clusteredVertx(
                new VertxOptions()
                        .setClusterManager(mgr)
                        .setMetricsOptions(new DropwizardMetricsOptions().setEnabled(true))
                        .setWorkerPoolSize(200)
                        .setEventLoopPoolSize(20)
                        .setInternalBlockingPoolSize(500),
                res -> {
                    if (res.succeeded()) {
                        deployVerticles(res.result());

                        res.result().sharedData().getClusterWideMap("grpc-server", asyncGetEvent -> {
                            IMap<Object, Object> addresses = mgr.getHazelcastInstance().getMap("grpc-server");
                            asyncGetEvent.result().put("ipaddresses", addIpAddressFunction(mgr, addresses.get("ipaddresses")), asyncPutEvent -> {
                                if (asyncPutEvent.succeeded()) {
                                    System.out.println("gRPC server address added to cluster wide map");
                                } else {
                                    System.out.println("Bieda..");
                                }
                            });
                        });
                    } else {
                        throw new RuntimeException("Unable to setup clustered Vert.x instance", res.cause());
                    }
                }
        );

    }

    private static Set<URL> addIpAddressFunction(HazelcastClusterManager mgr, Object value) {
            Set<URL> result = (Set)value;
            if (value == null) {
                result = new HashSet<>();
            }

            Address address = mgr.getHazelcastInstance().getCluster().getLocalMember().getAddress();
            int port = Integer.getInteger("grpc.server.port", 7070);
            try {
//                result.add(new URL("http", address.getHost(), port, "/"));
                result.add(new URL("http", "127.0.0.1", port, "/"));
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }

            return result;
    }

    private static void deployVerticles(Vertx vertx) {
        vertx.deployVerticle(Consumer.class.getName(), new DeploymentOptions()
                .setInstances(50)
                .setMultiThreaded(true)
                .setWorker(true)
                .setWorkerPoolSize(100)
        );
        vertx.deployVerticle(Proxy.class.getName(), new DeploymentOptions()
                .setInstances(5)
                .setWorker(true)
        );


        vertx.deployVerticle(new VertxHttpServer());
        vertx.deployVerticle(new VertxGrpcServer());

        JsonUtils.mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        System.out.println("Vertx server started");
    }
}
