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
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SchedulerApp {

    private static final Logger LOG = LoggerFactory.getLogger(SchedulerApp.class);

    public static final Vertx VERTX = Vertx.factory.vertx(
            new VertxOptions()
                    .setWorkerPoolSize(100)
                    .setEventLoopPoolSize(16)
                    .setInternalBlockingPoolSize(100)
    );

    public static void main(String... args) {
        VERTX.deployVerticle(new VertxHttpServer());
        VERTX.deployVerticle(HttpBulkClient.class.getName(), new DeploymentOptions().setInstances(1));
        VERTX.deployVerticle(HttpLoadClient.class.getName(), new DeploymentOptions().setInstances(1));
        VERTX.deployVerticle(GrpcLoadClient.class.getName(), new DeploymentOptions().setInstances(1));
        VERTX.deployVerticle(Collector.class.getName());

        JsonUtils.mapper = new ObjectMapper(new CBORFactory()).registerModule(new AfterburnerModule());

        JsonUtils.mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        LOG.info("Vertx scheduler started");
    }

}
