package com.github.rfoltyns.vertx.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 * <pre>
 * The greeting service definition.
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.9.0)",
    comments = "Source: GrpcService.proto")
public final class ReceiverGrpc {

  private ReceiverGrpc() {}

  private static <T> io.grpc.stub.StreamObserver<T> toObserver(final io.vertx.core.Handler<io.vertx.core.AsyncResult<T>> handler) {
    return new io.grpc.stub.StreamObserver<T>() {
      private volatile boolean resolved = false;
      @Override
      public void onNext(T value) {
        if (!resolved) {
          resolved = true;
          handler.handle(io.vertx.core.Future.succeededFuture(value));
        }
      }

      @Override
      public void onError(Throwable t) {
        if (!resolved) {
          resolved = true;
          handler.handle(io.vertx.core.Future.failedFuture(t));
        }
      }

      @Override
      public void onCompleted() {
        if (!resolved) {
          resolved = true;
          handler.handle(io.vertx.core.Future.succeededFuture());
        }
      }
    };
  }

  public static final String SERVICE_NAME = "com.github.rfoltyns.vertx.grpc.Receiver";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getMeasureMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.github.rfoltyns.vertx.grpc.MeasureRequest,
      com.github.rfoltyns.vertx.grpc.MeasureReply> METHOD_MEASURE = getMeasureMethod();

  private static volatile io.grpc.MethodDescriptor<com.github.rfoltyns.vertx.grpc.MeasureRequest,
      com.github.rfoltyns.vertx.grpc.MeasureReply> getMeasureMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.github.rfoltyns.vertx.grpc.MeasureRequest,
      com.github.rfoltyns.vertx.grpc.MeasureReply> getMeasureMethod() {
    io.grpc.MethodDescriptor<com.github.rfoltyns.vertx.grpc.MeasureRequest, com.github.rfoltyns.vertx.grpc.MeasureReply> getMeasureMethod;
    if ((getMeasureMethod = ReceiverGrpc.getMeasureMethod) == null) {
      synchronized (ReceiverGrpc.class) {
        if ((getMeasureMethod = ReceiverGrpc.getMeasureMethod) == null) {
          ReceiverGrpc.getMeasureMethod = getMeasureMethod = 
              io.grpc.MethodDescriptor.<com.github.rfoltyns.vertx.grpc.MeasureRequest, com.github.rfoltyns.vertx.grpc.MeasureReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.github.rfoltyns.vertx.grpc.Receiver", "Measure"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.github.rfoltyns.vertx.grpc.MeasureRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.github.rfoltyns.vertx.grpc.MeasureReply.getDefaultInstance()))
                  .setSchemaDescriptor(new ReceiverMethodDescriptorSupplier("Measure"))
                  .build();
          }
        }
     }
     return getMeasureMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ReceiverStub newStub(io.grpc.Channel channel) {
    return new ReceiverStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ReceiverBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new ReceiverBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ReceiverFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new ReceiverFutureStub(channel);
  }

  /**
   * Creates a new vertx stub that supports all call types for the service
   */
  public static ReceiverVertxStub newVertxStub(io.grpc.Channel channel) {
    return new ReceiverVertxStub(channel);
  }

  /**
   * <pre>
   * The greeting service definition.
   * </pre>
   */
  public static abstract class ReceiverImplBase implements io.grpc.BindableService {

    /**
     */
    public void measure(com.github.rfoltyns.vertx.grpc.MeasureRequest request,
        io.grpc.stub.StreamObserver<com.github.rfoltyns.vertx.grpc.MeasureReply> responseObserver) {
      asyncUnimplementedUnaryCall(getMeasureMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getMeasureMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.github.rfoltyns.vertx.grpc.MeasureRequest,
                com.github.rfoltyns.vertx.grpc.MeasureReply>(
                  this, METHODID_MEASURE)))
          .build();
    }
  }

  /**
   * <pre>
   * The greeting service definition.
   * </pre>
   */
  public static final class ReceiverStub extends io.grpc.stub.AbstractStub<ReceiverStub> {
    public ReceiverStub(io.grpc.Channel channel) {
      super(channel);
    }

    public ReceiverStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ReceiverStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ReceiverStub(channel, callOptions);
    }

    /**
     */
    public void measure(com.github.rfoltyns.vertx.grpc.MeasureRequest request,
        io.grpc.stub.StreamObserver<com.github.rfoltyns.vertx.grpc.MeasureReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getMeasureMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * <pre>
   * The greeting service definition.
   * </pre>
   */
  public static final class ReceiverBlockingStub extends io.grpc.stub.AbstractStub<ReceiverBlockingStub> {
    public ReceiverBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    public ReceiverBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ReceiverBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ReceiverBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.github.rfoltyns.vertx.grpc.MeasureReply measure(com.github.rfoltyns.vertx.grpc.MeasureRequest request) {
      return blockingUnaryCall(
          getChannel(), getMeasureMethod(), getCallOptions(), request);
    }
  }

  /**
   * <pre>
   * The greeting service definition.
   * </pre>
   */
  public static final class ReceiverFutureStub extends io.grpc.stub.AbstractStub<ReceiverFutureStub> {
    public ReceiverFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    public ReceiverFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ReceiverFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ReceiverFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.github.rfoltyns.vertx.grpc.MeasureReply> measure(
        com.github.rfoltyns.vertx.grpc.MeasureRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getMeasureMethod(), getCallOptions()), request);
    }
  }

  /**
   * <pre>
   * The greeting service definition.
   * </pre>
   */
  public static abstract class ReceiverVertxImplBase implements io.grpc.BindableService {

    /**
     */
    public void measure(com.github.rfoltyns.vertx.grpc.MeasureRequest request,
        io.vertx.core.Future<com.github.rfoltyns.vertx.grpc.MeasureReply> response) {
      asyncUnimplementedUnaryCall(getMeasureMethod(), ReceiverGrpc.toObserver(response.completer()));
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getMeasureMethod(),
            asyncUnaryCall(
              new VertxMethodHandlers<
                com.github.rfoltyns.vertx.grpc.MeasureRequest,
                com.github.rfoltyns.vertx.grpc.MeasureReply>(
                  this, METHODID_MEASURE)))
          .build();
    }
  }

  /**
   * <pre>
   * The greeting service definition.
   * </pre>
   */
  public static final class ReceiverVertxStub extends io.grpc.stub.AbstractStub<ReceiverVertxStub> {
    public ReceiverVertxStub(io.grpc.Channel channel) {
      super(channel);
    }

    public ReceiverVertxStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ReceiverVertxStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ReceiverVertxStub(channel, callOptions);
    }

    /**
     */
    public void measure(com.github.rfoltyns.vertx.grpc.MeasureRequest request,
        io.vertx.core.Handler<io.vertx.core.AsyncResult<com.github.rfoltyns.vertx.grpc.MeasureReply>> response) {
      asyncUnaryCall(
          getChannel().newCall(getMeasureMethod(), getCallOptions()), request, ReceiverGrpc.toObserver(response));
    }
  }

  private static final int METHODID_MEASURE = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final ReceiverImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(ReceiverImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_MEASURE:
          serviceImpl.measure((com.github.rfoltyns.vertx.grpc.MeasureRequest) request,
              (io.grpc.stub.StreamObserver<com.github.rfoltyns.vertx.grpc.MeasureReply>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static final class VertxMethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final ReceiverVertxImplBase serviceImpl;
    private final int methodId;

    VertxMethodHandlers(ReceiverVertxImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_MEASURE:
          serviceImpl.measure((com.github.rfoltyns.vertx.grpc.MeasureRequest) request,
              (io.vertx.core.Future<com.github.rfoltyns.vertx.grpc.MeasureReply>) io.vertx.core.Future.<com.github.rfoltyns.vertx.grpc.MeasureReply>future().setHandler(ar -> {
                if (ar.succeeded()) {
                  ((io.grpc.stub.StreamObserver<com.github.rfoltyns.vertx.grpc.MeasureReply>) responseObserver).onNext(ar.result());
                  responseObserver.onCompleted();
                } else {
                  responseObserver.onError(ar.cause());
                }
              }));
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class ReceiverBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ReceiverBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.github.rfoltyns.vertx.grpc.GrpcProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Receiver");
    }
  }

  private static final class ReceiverFileDescriptorSupplier
      extends ReceiverBaseDescriptorSupplier {
    ReceiverFileDescriptorSupplier() {}
  }

  private static final class ReceiverMethodDescriptorSupplier
      extends ReceiverBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    ReceiverMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (ReceiverGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ReceiverFileDescriptorSupplier())
              .addMethod(getMeasureMethod())
              .build();
        }
      }
    }
    return result;
  }
}
