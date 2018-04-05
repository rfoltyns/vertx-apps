package com.github.rfoltyns;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import com.github.rfoltyns.vertx.Collector;
import com.github.rfoltyns.vertx.GrpcLoadClient;
import com.github.rfoltyns.vertx.JsonUtils;
import com.github.rfoltyns.vertx.HttpBulkClient;
import com.github.rfoltyns.vertx.VertxHttpServer;
import com.github.rfoltyns.vertx.HttpLoadClient;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiFunction;

public class SchedulerApp {

    private static final Logger LOG = LoggerFactory.getLogger(SchedulerApp.class);


    public static void main(String... args) {

//        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();

        HazelcastClusterManager mgr = new HazelcastClusterManager();
//        mgr.getConfig().getNetworkConfig().getPublicAddress();
        Vertx.clusteredVertx(
                new VertxOptions().setClusterManager(mgr)
                        .setWorkerPoolSize(100)
                        .setEventLoopPoolSize(16)
                        .setInternalBlockingPoolSize(100),
                res -> {
                    if (res.succeeded()) {
                        deployVerticles(res.result());
                    } else {
                        throw new RuntimeException("Unable to setup clustered Vert.x instance", res.cause());
                    }
                }
        );

        JsonUtils.mapper = new ObjectMapper(new CBORFactory()).registerModule(new AfterburnerModule());

        JsonUtils.mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        LOG.info("Vertx scheduler started");
    }

    private static void deployVerticles(Vertx vertx) {
        vertx.deployVerticle(new VertxHttpServer());
        vertx.deployVerticle(HttpBulkClient.class.getName(), new DeploymentOptions().setInstances(1));
        vertx.deployVerticle(HttpLoadClient.class.getName(), new DeploymentOptions().setInstances(1));
        vertx.deployVerticle(GrpcLoadClient.class.getName(), new DeploymentOptions().setInstances(1));
        vertx.deployVerticle(Collector.class.getName());

    }

}
