// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: GrpcService.proto

package com.github.rfoltyns.vertx.grpc;

/**
 * <pre>
 * The response message containing the greetings
 * </pre>
 *
 * Protobuf type {@code com.github.rfoltyns.vertx.grpc.MeasureReply}
 */
public  final class MeasureReply extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:com.github.rfoltyns.vertx.grpc.MeasureReply)
    MeasureReplyOrBuilder {
  // Use MeasureReply.newBuilder() to construct.
  private MeasureReply(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private MeasureReply() {
    id_ = 0;
    createdAt_ = 0L;
    receivedAt_ = 0L;
    processedAt_ = 0L;
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return com.google.protobuf.UnknownFieldSet.getDefaultInstance();
  }
  private MeasureReply(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    int mutable_bitField0_ = 0;
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          default: {
            if (!input.skipField(tag)) {
              done = true;
            }
            break;
          }
          case 8: {

            id_ = input.readInt32();
            break;
          }
          case 24: {

            createdAt_ = input.readInt64();
            break;
          }
          case 32: {

            receivedAt_ = input.readInt64();
            break;
          }
          case 40: {

            processedAt_ = input.readInt64();
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.github.rfoltyns.vertx.grpc.GrpcProto.internal_static_com_github_rfoltyns_vertx_grpc_MeasureReply_descriptor;
  }

  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.github.rfoltyns.vertx.grpc.GrpcProto.internal_static_com_github_rfoltyns_vertx_grpc_MeasureReply_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.github.rfoltyns.vertx.grpc.MeasureReply.class, com.github.rfoltyns.vertx.grpc.MeasureReply.Builder.class);
  }

  public static final int ID_FIELD_NUMBER = 1;
  private int id_;
  /**
   * <code>int32 id = 1;</code>
   */
  public int getId() {
    return id_;
  }

  public static final int CREATEDAT_FIELD_NUMBER = 3;
  private long createdAt_;
  /**
   * <code>int64 createdAt = 3;</code>
   */
  public long getCreatedAt() {
    return createdAt_;
  }

  public static final int RECEIVEDAT_FIELD_NUMBER = 4;
  private long receivedAt_;
  /**
   * <code>int64 receivedAt = 4;</code>
   */
  public long getReceivedAt() {
    return receivedAt_;
  }

  public static final int PROCESSEDAT_FIELD_NUMBER = 5;
  private long processedAt_;
  /**
   * <code>int64 processedAt = 5;</code>
   */
  public long getProcessedAt() {
    return processedAt_;
  }

  private byte memoizedIsInitialized = -1;
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (id_ != 0) {
      output.writeInt32(1, id_);
    }
    if (createdAt_ != 0L) {
      output.writeInt64(3, createdAt_);
    }
    if (receivedAt_ != 0L) {
      output.writeInt64(4, receivedAt_);
    }
    if (processedAt_ != 0L) {
      output.writeInt64(5, processedAt_);
    }
  }

  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (id_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(1, id_);
    }
    if (createdAt_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt64Size(3, createdAt_);
    }
    if (receivedAt_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt64Size(4, receivedAt_);
    }
    if (processedAt_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt64Size(5, processedAt_);
    }
    memoizedSize = size;
    return size;
  }

  private static final long serialVersionUID = 0L;
  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof com.github.rfoltyns.vertx.grpc.MeasureReply)) {
      return super.equals(obj);
    }
    com.github.rfoltyns.vertx.grpc.MeasureReply other = (com.github.rfoltyns.vertx.grpc.MeasureReply) obj;

    boolean result = true;
    result = result && (getId()
        == other.getId());
    result = result && (getCreatedAt()
        == other.getCreatedAt());
    result = result && (getReceivedAt()
        == other.getReceivedAt());
    result = result && (getProcessedAt()
        == other.getProcessedAt());
    return result;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    hash = (37 * hash) + ID_FIELD_NUMBER;
    hash = (53 * hash) + getId();
    hash = (37 * hash) + CREATEDAT_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getCreatedAt());
    hash = (37 * hash) + RECEIVEDAT_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getReceivedAt());
    hash = (37 * hash) + PROCESSEDAT_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getProcessedAt());
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.github.rfoltyns.vertx.grpc.MeasureReply parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.github.rfoltyns.vertx.grpc.MeasureReply parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.github.rfoltyns.vertx.grpc.MeasureReply parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.github.rfoltyns.vertx.grpc.MeasureReply parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.github.rfoltyns.vertx.grpc.MeasureReply parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.github.rfoltyns.vertx.grpc.MeasureReply parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.github.rfoltyns.vertx.grpc.MeasureReply parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.github.rfoltyns.vertx.grpc.MeasureReply parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.github.rfoltyns.vertx.grpc.MeasureReply parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.github.rfoltyns.vertx.grpc.MeasureReply parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(com.github.rfoltyns.vertx.grpc.MeasureReply prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * <pre>
   * The response message containing the greetings
   * </pre>
   *
   * Protobuf type {@code com.github.rfoltyns.vertx.grpc.MeasureReply}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:com.github.rfoltyns.vertx.grpc.MeasureReply)
      com.github.rfoltyns.vertx.grpc.MeasureReplyOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.github.rfoltyns.vertx.grpc.GrpcProto.internal_static_com_github_rfoltyns_vertx_grpc_MeasureReply_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.github.rfoltyns.vertx.grpc.GrpcProto.internal_static_com_github_rfoltyns_vertx_grpc_MeasureReply_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.github.rfoltyns.vertx.grpc.MeasureReply.class, com.github.rfoltyns.vertx.grpc.MeasureReply.Builder.class);
    }

    // Construct using com.github.rfoltyns.vertx.grpc.MeasureReply.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
      }
    }
    public Builder clear() {
      super.clear();
      id_ = 0;

      createdAt_ = 0L;

      receivedAt_ = 0L;

      processedAt_ = 0L;

      return this;
    }

    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.github.rfoltyns.vertx.grpc.GrpcProto.internal_static_com_github_rfoltyns_vertx_grpc_MeasureReply_descriptor;
    }

    public com.github.rfoltyns.vertx.grpc.MeasureReply getDefaultInstanceForType() {
      return com.github.rfoltyns.vertx.grpc.MeasureReply.getDefaultInstance();
    }

    public com.github.rfoltyns.vertx.grpc.MeasureReply build() {
      com.github.rfoltyns.vertx.grpc.MeasureReply result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    public com.github.rfoltyns.vertx.grpc.MeasureReply buildPartial() {
      com.github.rfoltyns.vertx.grpc.MeasureReply result = new com.github.rfoltyns.vertx.grpc.MeasureReply(this);
      result.id_ = id_;
      result.createdAt_ = createdAt_;
      result.receivedAt_ = receivedAt_;
      result.processedAt_ = processedAt_;
      onBuilt();
      return result;
    }

    public Builder clone() {
      return (Builder) super.clone();
    }
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        Object value) {
      return (Builder) super.setField(field, value);
    }
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return (Builder) super.clearField(field);
    }
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return (Builder) super.clearOneof(oneof);
    }
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, Object value) {
      return (Builder) super.setRepeatedField(field, index, value);
    }
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        Object value) {
      return (Builder) super.addRepeatedField(field, value);
    }
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof com.github.rfoltyns.vertx.grpc.MeasureReply) {
        return mergeFrom((com.github.rfoltyns.vertx.grpc.MeasureReply)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.github.rfoltyns.vertx.grpc.MeasureReply other) {
      if (other == com.github.rfoltyns.vertx.grpc.MeasureReply.getDefaultInstance()) return this;
      if (other.getId() != 0) {
        setId(other.getId());
      }
      if (other.getCreatedAt() != 0L) {
        setCreatedAt(other.getCreatedAt());
      }
      if (other.getReceivedAt() != 0L) {
        setReceivedAt(other.getReceivedAt());
      }
      if (other.getProcessedAt() != 0L) {
        setProcessedAt(other.getProcessedAt());
      }
      onChanged();
      return this;
    }

    public final boolean isInitialized() {
      return true;
    }

    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      com.github.rfoltyns.vertx.grpc.MeasureReply parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (com.github.rfoltyns.vertx.grpc.MeasureReply) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private int id_ ;
    /**
     * <code>int32 id = 1;</code>
     */
    public int getId() {
      return id_;
    }
    /**
     * <code>int32 id = 1;</code>
     */
    public Builder setId(int value) {
      
      id_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>int32 id = 1;</code>
     */
    public Builder clearId() {
      
      id_ = 0;
      onChanged();
      return this;
    }

    private long createdAt_ ;
    /**
     * <code>int64 createdAt = 3;</code>
     */
    public long getCreatedAt() {
      return createdAt_;
    }
    /**
     * <code>int64 createdAt = 3;</code>
     */
    public Builder setCreatedAt(long value) {
      
      createdAt_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>int64 createdAt = 3;</code>
     */
    public Builder clearCreatedAt() {
      
      createdAt_ = 0L;
      onChanged();
      return this;
    }

    private long receivedAt_ ;
    /**
     * <code>int64 receivedAt = 4;</code>
     */
    public long getReceivedAt() {
      return receivedAt_;
    }
    /**
     * <code>int64 receivedAt = 4;</code>
     */
    public Builder setReceivedAt(long value) {
      
      receivedAt_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>int64 receivedAt = 4;</code>
     */
    public Builder clearReceivedAt() {
      
      receivedAt_ = 0L;
      onChanged();
      return this;
    }

    private long processedAt_ ;
    /**
     * <code>int64 processedAt = 5;</code>
     */
    public long getProcessedAt() {
      return processedAt_;
    }
    /**
     * <code>int64 processedAt = 5;</code>
     */
    public Builder setProcessedAt(long value) {
      
      processedAt_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>int64 processedAt = 5;</code>
     */
    public Builder clearProcessedAt() {
      
      processedAt_ = 0L;
      onChanged();
      return this;
    }
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return this;
    }

    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return this;
    }


    // @@protoc_insertion_point(builder_scope:com.github.rfoltyns.vertx.grpc.MeasureReply)
  }

  // @@protoc_insertion_point(class_scope:com.github.rfoltyns.vertx.grpc.MeasureReply)
  private static final com.github.rfoltyns.vertx.grpc.MeasureReply DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.github.rfoltyns.vertx.grpc.MeasureReply();
  }

  public static com.github.rfoltyns.vertx.grpc.MeasureReply getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<MeasureReply>
      PARSER = new com.google.protobuf.AbstractParser<MeasureReply>() {
    public MeasureReply parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
        return new MeasureReply(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<MeasureReply> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<MeasureReply> getParserForType() {
    return PARSER;
  }

  public com.github.rfoltyns.vertx.grpc.MeasureReply getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

