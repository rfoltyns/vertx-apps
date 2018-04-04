package com.github.rfoltyns.vertx;

import com.github.rfoltyns.vertx.grpc.MeasureReply;
import com.github.rfoltyns.vertx.grpc.MeasureReplyOrBuilder;
import com.github.rfoltyns.vertx.grpc.MeasureRequest;
import com.github.rfoltyns.vertx.grpc.ReceiverGrpc;
import examples.GreeterGrpc;
import examples.HelloReply;
import examples.HelloRequest;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.grpc.VertxServer;
import io.vertx.grpc.VertxServerBuilder;

public class VertxGrpcServer extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        GreeterGrpc.GreeterVertxImplBase service = new GreeterGrpc.GreeterVertxImplBase() {
            @Override
            public void sayHello(HelloRequest request, Future<HelloReply> future) {
                future.complete(HelloReply.newBuilder().setMessage(request.getName()).build());
            }
        };

        ReceiverGrpc.ReceiverVertxImplBase measureService = new ReceiverGrpc.ReceiverVertxImplBase() {
            @Override
            public void measure(MeasureRequest request, Future<MeasureReply> future) {

                MeasureReply reply = MeasureReply.newBuilder()
                        .setId(request.getId())
                        .setCreatedAt(request.getCreatedAt())
                        .setReceivedAt(System.currentTimeMillis())
                        .build();

                future.complete(reply);
            }
        };

        VertxServer rpcServer = VertxServerBuilder
                .forAddress(vertx, "localhost", 7070)
//                .addService(service)
                .addService(measureService)
                .build();

        // Start is asynchronous
        rpcServer.start();
    }

}
