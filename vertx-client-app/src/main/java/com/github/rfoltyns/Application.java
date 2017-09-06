package com.github.rfoltyns;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.rfoltyns.vertx.Collector;
import com.github.rfoltyns.vertx.VertxBulkClient;
import com.github.rfoltyns.vertx.VertxHttpServer;
import com.github.rfoltyns.vertx.VertxLoadClient;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.Json;
import org.slf4j.LoggerFactory;

public class Application {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(Application.class);

    public static final Vertx VERTX = Vertx.factory.vertx(
            new VertxOptions()
                    .setWorkerPoolSize(100)
                    .setEventLoopPoolSize(100)
                    .setInternalBlockingPoolSize(100)
    );

    public static void main(String... args) throws InterruptedException {
        VERTX.deployVerticle(new VertxHttpServer());
        VERTX.deployVerticle(VertxBulkClient.class.getName(), new DeploymentOptions().setInstances(1));
        VERTX.deployVerticle(VertxLoadClient.class.getName(), new DeploymentOptions().setInstances(1));
        VERTX.deployVerticle(Collector.class.getName());

        Json.mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        LOG.info("Vertx server started");
    }

}
