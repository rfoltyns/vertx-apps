package com.github.rfoltyns;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import com.github.rfoltyns.vertx.Consumer;
import com.github.rfoltyns.vertx.Proxy;
import com.github.rfoltyns.vertx.VertxHttpServer;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.dropwizard.DropwizardMetricsOptions;
import io.vertx.ext.dropwizard.MetricsService;
import org.slf4j.LoggerFactory;

public class Application {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger("elasticsearch");

    public static void main(String... args) throws InterruptedException {
        System.out.println("Starting server");
        Vertx vertx = Vertx.factory.vertx(
                new VertxOptions()
                        .setMetricsOptions(new DropwizardMetricsOptions().setEnabled(true))
                        .setWorkerPoolSize(20)
//                        .setEventLoopPoolSize(1)
//                        .setInternalBlockingPoolSize(20)
        );
        vertx.deployVerticle(Consumer.class.getName(), new DeploymentOptions()
                        .setInstances(50)
//                .setMultiThreaded(true)
                .setWorker(true)
//                .setWorkerPoolSize(400)
        );
        vertx.deployVerticle(Proxy.class.getName(), new DeploymentOptions()
                .setInstances(50)
                .setWorker(true)
        );

//        Json.mapper = new ObjectMapper(new CBORFactory()).registerModule(new AfterburnerModule());

        vertx.deployVerticle(new VertxHttpServer());

        Json.mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        System.out.println("Vertx server started");
    }

}
