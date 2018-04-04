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
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.Json;
import io.vertx.ext.dropwizard.DropwizardMetricsOptions;
import org.slf4j.LoggerFactory;

public class Application {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger("elasticsearch");

    public static void main(String... args) throws InterruptedException {
        System.out.println("Starting server");
        JsonUtils.mapper = new ObjectMapper(new CBORFactory()).registerModule(new AfterburnerModule());

        Vertx vertx = Vertx.factory.vertx(
                new VertxOptions()
                        .setMetricsOptions(new DropwizardMetricsOptions().setEnabled(true))
                        .setWorkerPoolSize(200)
                        .setEventLoopPoolSize(20)
                        .setInternalBlockingPoolSize(500)
        );
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
