package com.github.rfoltyns;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.rfoltyns.vertx.Consumer;
import com.github.rfoltyns.vertx.VertxHttpServer;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.dropwizard.MetricsService;
import org.slf4j.LoggerFactory;

public class Application {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger("elasticsearch");

    public static void main(String... args) throws InterruptedException {
        System.out.println("Starting server");
        Vertx vertx = Vertx.factory.vertx(
                new VertxOptions()
//                        .setMetricsOptions(new MetricsOptions().setEnabled(true))
                        .setWorkerPoolSize(200)
//                        .setEventLoopPoolSize(200)
//                        .setInternalBlockingPoolSize(200)
        );
        vertx.deployVerticle(Consumer.class.getName(), new DeploymentOptions().setInstances(20)
                .setMultiThreaded(true)
                .setWorker(true)
//                .setWorkerPoolSize(400)
        );
        vertx.deployVerticle(new VertxHttpServer());
        MetricsService metricsService = MetricsService.create(vertx);
        JsonObject metrics = metricsService.getMetricsSnapshot(vertx);
        System.out.println(metrics);

        Json.mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        System.out.println("Vertx server started");
    }

}
