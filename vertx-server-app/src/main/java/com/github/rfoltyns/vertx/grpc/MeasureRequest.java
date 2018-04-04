// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: GrpcService.proto

package com.github.rfoltyns.vertx.grpc;

/**
 * <pre>
 * The request message containing the user's name.
 * </pre>
 *
 * Protobuf type {@code com.github.rfoltyns.vertx.grpc.MeasureRequest}
 */
public  final class MeasureRequest extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:com.github.rfoltyns.vertx.grpc.MeasureRequest)
    MeasureRequestOrBuilder {
  // Use MeasureRequest.newBuilder() to construct.
  private MeasureRequest(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private MeasureRequest() {
    id_ = 0;
    consumer_ = "";
    createdAt_ = 0L;
    receivedAt_ = 0L;
    processedAt_ = 0L;
    delayMillis_ = 0;
    delayDeviation_ = 0;
    numberOfHops_ = 0;
    data_ = com.google.protobuf.ByteString.EMPTY;
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return com.google.protobuf.UnknownFieldSet.getDefaultInstance();
  }
  private MeasureRequest(
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
          case 18: {
            java.lang.String s = input.readStringRequireUtf8();

            consumer_ = s;
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
          case 48: {

            delayMillis_ = input.readInt32();
            break;
          }
          case 56: {

            delayDeviation_ = input.readInt32();
            break;
          }
          case 64: {

            numberOfHops_ = input.readInt32();
            break;
          }
          case 74: {

            data_ = input.readBytes();
            break;
          }
          case 82: {
            if (!((mutable_bitField0_ & 0x00000200) == 0x00000200)) {
              sampleMap_ = com.google.protobuf.MapField.newMapField(
                  SampleMapDefaultEntryHolder.defaultEntry);
              mutable_bitField0_ |= 0x00000200;
            }
            com.google.protobuf.MapEntry<java.lang.String, java.lang.String>
            sampleMap__ = input.readMessage(
                SampleMapDefaultEntryHolder.defaultEntry.getParserForType(), extensionRegistry);
            sampleMap_.getMutableMap().put(
                sampleMap__.getKey(), sampleMap__.getValue());
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
    return com.github.rfoltyns.vertx.grpc.GrpcProto.internal_static_com_github_rfoltyns_vertx_grpc_MeasureRequest_descriptor;
  }

  @SuppressWarnings({"rawtypes"})
  protected com.google.protobuf.MapField internalGetMapField(
      int number) {
    switch (number) {
      case 10:
        return internalGetSampleMap();
      default:
        throw new RuntimeException(
            "Invalid map field number: " + number);
    }
  }
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.github.rfoltyns.vertx.grpc.GrpcProto.internal_static_com_github_rfoltyns_vertx_grpc_MeasureRequest_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.github.rfoltyns.vertx.grpc.MeasureRequest.class, com.github.rfoltyns.vertx.grpc.MeasureRequest.Builder.class);
  }

  private int bitField0_;
  public static final int ID_FIELD_NUMBER = 1;
  private int id_;
  /**
   * <code>int32 id = 1;</code>
   */
  public int getId() {
    return id_;
  }

  public static final int CONSUMER_FIELD_NUMBER = 2;
  private volatile java.lang.Object consumer_;
  /**
   * <code>string consumer = 2;</code>
   */
  public java.lang.String getConsumer() {
    java.lang.Object ref = consumer_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      consumer_ = s;
      return s;
    }
  }
  /**
   * <code>string consumer = 2;</code>
   */
  public com.google.protobuf.ByteString
      getConsumerBytes() {
    java.lang.Object ref = consumer_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      consumer_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
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

  public static final int DELAYMILLIS_FIELD_NUMBER = 6;
  private int delayMillis_;
  /**
   * <code>int32 delayMillis = 6;</code>
   */
  public int getDelayMillis() {
    return delayMillis_;
  }

  public static final int DELAYDEVIATION_FIELD_NUMBER = 7;
  private int delayDeviation_;
  /**
   * <code>int32 delayDeviation = 7;</code>
   */
  public int getDelayDeviation() {
    return delayDeviation_;
  }

  public static final int NUMBEROFHOPS_FIELD_NUMBER = 8;
  private int numberOfHops_;
  /**
   * <code>int32 numberOfHops = 8;</code>
   */
  public int getNumberOfHops() {
    return numberOfHops_;
  }

  public static final int DATA_FIELD_NUMBER = 9;
  private com.google.protobuf.ByteString data_;
  /**
   * <code>bytes data = 9;</code>
   */
  public com.google.protobuf.ByteString getData() {
    return data_;
  }

  public static final int SAMPLEMAP_FIELD_NUMBER = 10;
  private static final class SampleMapDefaultEntryHolder {
    static final com.google.protobuf.MapEntry<
        java.lang.String, java.lang.String> defaultEntry =
            com.google.protobuf.MapEntry
            .<java.lang.String, java.lang.String>newDefaultInstance(
                com.github.rfoltyns.vertx.grpc.GrpcProto.internal_static_com_github_rfoltyns_vertx_grpc_MeasureRequest_SampleMapEntry_descriptor, 
                com.google.protobuf.WireFormat.FieldType.STRING,
                "",
                com.google.protobuf.WireFormat.FieldType.STRING,
                "");
  }
  private com.google.protobuf.MapField<
      java.lang.String, java.lang.String> sampleMap_;
  private com.google.protobuf.MapField<java.lang.String, java.lang.String>
  internalGetSampleMap() {
    if (sampleMap_ == null) {
      return com.google.protobuf.MapField.emptyMapField(
          SampleMapDefaultEntryHolder.defaultEntry);
    }
    return sampleMap_;
  }

  public int getSampleMapCount() {
    return internalGetSampleMap().getMap().size();
  }
  /**
   * <code>map&lt;string, string&gt; sampleMap = 10;</code>
   */

  public boolean containsSampleMap(
      java.lang.String key) {
    if (key == null) { throw new java.lang.NullPointerException(); }
    return internalGetSampleMap().getMap().containsKey(key);
  }
  /**
   * Use {@link #getSampleMapMap()} instead.
   */
  @java.lang.Deprecated
  public java.util.Map<java.lang.String, java.lang.String> getSampleMap() {
    return getSampleMapMap();
  }
  /**
   * <code>map&lt;string, string&gt; sampleMap = 10;</code>
   */

  public java.util.Map<java.lang.String, java.lang.String> getSampleMapMap() {
    return internalGetSampleMap().getMap();
  }
  /**
   * <code>map&lt;string, string&gt; sampleMap = 10;</code>
   */

  public java.lang.String getSampleMapOrDefault(
      java.lang.String key,
      java.lang.String defaultValue) {
    if (key == null) { throw new java.lang.NullPointerException(); }
    java.util.Map<java.lang.String, java.lang.String> map =
        internalGetSampleMap().getMap();
    return map.containsKey(key) ? map.get(key) : defaultValue;
  }
  /**
   * <code>map&lt;string, string&gt; sampleMap = 10;</code>
   */

  public java.lang.String getSampleMapOrThrow(
      java.lang.String key) {
    if (key == null) { throw new java.lang.NullPointerException(); }
    java.util.Map<java.lang.String, java.lang.String> map =
        internalGetSampleMap().getMap();
    if (!map.containsKey(key)) {
      throw new java.lang.IllegalArgumentException();
    }
    return map.get(key);
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
    if (!getConsumerBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 2, consumer_);
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
    if (delayMillis_ != 0) {
      output.writeInt32(6, delayMillis_);
    }
    if (delayDeviation_ != 0) {
      output.writeInt32(7, delayDeviation_);
    }
    if (numberOfHops_ != 0) {
      output.writeInt32(8, numberOfHops_);
    }
    if (!data_.isEmpty()) {
      output.writeBytes(9, data_);
    }
    com.google.protobuf.GeneratedMessageV3
      .serializeStringMapTo(
        output,
        internalGetSampleMap(),
        SampleMapDefaultEntryHolder.defaultEntry,
        10);
  }

  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (id_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(1, id_);
    }
    if (!getConsumerBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, consumer_);
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
    if (delayMillis_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(6, delayMillis_);
    }
    if (delayDeviation_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(7, delayDeviation_);
    }
    if (numberOfHops_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(8, numberOfHops_);
    }
    if (!data_.isEmpty()) {
      size += com.google.protobuf.CodedOutputStream
        .computeBytesSize(9, data_);
    }
    for (java.util.Map.Entry<java.lang.String, java.lang.String> entry
         : internalGetSampleMap().getMap().entrySet()) {
      com.google.protobuf.MapEntry<java.lang.String, java.lang.String>
      sampleMap__ = SampleMapDefaultEntryHolder.defaultEntry.newBuilderForType()
          .setKey(entry.getKey())
          .setValue(entry.getValue())
          .build();
      size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(10, sampleMap__);
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
    if (!(obj instanceof com.github.rfoltyns.vertx.grpc.MeasureRequest)) {
      return super.equals(obj);
    }
    com.github.rfoltyns.vertx.grpc.MeasureRequest other = (com.github.rfoltyns.vertx.grpc.MeasureRequest) obj;

    boolean result = true;
    result = result && (getId()
        == other.getId());
    result = result && getConsumer()
        .equals(other.getConsumer());
    result = result && (getCreatedAt()
        == other.getCreatedAt());
    result = result && (getReceivedAt()
        == other.getReceivedAt());
    result = result && (getProcessedAt()
        == other.getProcessedAt());
    result = result && (getDelayMillis()
        == other.getDelayMillis());
    result = result && (getDelayDeviation()
        == other.getDelayDeviation());
    result = result && (getNumberOfHops()
        == other.getNumberOfHops());
    result = result && getData()
        .equals(other.getData());
    result = result && internalGetSampleMap().equals(
        other.internalGetSampleMap());
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
    hash = (37 * hash) + CONSUMER_FIELD_NUMBER;
    hash = (53 * hash) + getConsumer().hashCode();
    hash = (37 * hash) + CREATEDAT_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getCreatedAt());
    hash = (37 * hash) + RECEIVEDAT_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getReceivedAt());
    hash = (37 * hash) + PROCESSEDAT_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getProcessedAt());
    hash = (37 * hash) + DELAYMILLIS_FIELD_NUMBER;
    hash = (53 * hash) + getDelayMillis();
    hash = (37 * hash) + DELAYDEVIATION_FIELD_NUMBER;
    hash = (53 * hash) + getDelayDeviation();
    hash = (37 * hash) + NUMBEROFHOPS_FIELD_NUMBER;
    hash = (53 * hash) + getNumberOfHops();
    hash = (37 * hash) + DATA_FIELD_NUMBER;
    hash = (53 * hash) + getData().hashCode();
    if (!internalGetSampleMap().getMap().isEmpty()) {
      hash = (37 * hash) + SAMPLEMAP_FIELD_NUMBER;
      hash = (53 * hash) + internalGetSampleMap().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.github.rfoltyns.vertx.grpc.MeasureRequest parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.github.rfoltyns.vertx.grpc.MeasureRequest parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.github.rfoltyns.vertx.grpc.MeasureRequest parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.github.rfoltyns.vertx.grpc.MeasureRequest parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.github.rfoltyns.vertx.grpc.MeasureRequest parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.github.rfoltyns.vertx.grpc.MeasureRequest parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.github.rfoltyns.vertx.grpc.MeasureRequest parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.github.rfoltyns.vertx.grpc.MeasureRequest parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.github.rfoltyns.vertx.grpc.MeasureRequest parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.github.rfoltyns.vertx.grpc.MeasureRequest parseFrom(
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
  public static Builder newBuilder(com.github.rfoltyns.vertx.grpc.MeasureRequest prototype) {
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
   * The request message containing the user's name.
   * </pre>
   *
   * Protobuf type {@code com.github.rfoltyns.vertx.grpc.MeasureRequest}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:com.github.rfoltyns.vertx.grpc.MeasureRequest)
      com.github.rfoltyns.vertx.grpc.MeasureRequestOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.github.rfoltyns.vertx.grpc.GrpcProto.internal_static_com_github_rfoltyns_vertx_grpc_MeasureRequest_descriptor;
    }

    @SuppressWarnings({"rawtypes"})
    protected com.google.protobuf.MapField internalGetMapField(
        int number) {
      switch (number) {
        case 10:
          return internalGetSampleMap();
        default:
          throw new RuntimeException(
              "Invalid map field number: " + number);
      }
    }
    @SuppressWarnings({"rawtypes"})
    protected com.google.protobuf.MapField internalGetMutableMapField(
        int number) {
      switch (number) {
        case 10:
          return internalGetMutableSampleMap();
        default:
          throw new RuntimeException(
              "Invalid map field number: " + number);
      }
    }
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.github.rfoltyns.vertx.grpc.GrpcProto.internal_static_com_github_rfoltyns_vertx_grpc_MeasureRequest_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.github.rfoltyns.vertx.grpc.MeasureRequest.class, com.github.rfoltyns.vertx.grpc.MeasureRequest.Builder.class);
    }

    // Construct using com.github.rfoltyns.vertx.grpc.MeasureRequest.newBuilder()
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

      consumer_ = "";

      createdAt_ = 0L;

      receivedAt_ = 0L;

      processedAt_ = 0L;

      delayMillis_ = 0;

      delayDeviation_ = 0;

      numberOfHops_ = 0;

      data_ = com.google.protobuf.ByteString.EMPTY;

      internalGetMutableSampleMap().clear();
      return this;
    }

    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.github.rfoltyns.vertx.grpc.GrpcProto.internal_static_com_github_rfoltyns_vertx_grpc_MeasureRequest_descriptor;
    }

    public com.github.rfoltyns.vertx.grpc.MeasureRequest getDefaultInstanceForType() {
      return com.github.rfoltyns.vertx.grpc.MeasureRequest.getDefaultInstance();
    }

    public com.github.rfoltyns.vertx.grpc.MeasureRequest build() {
      com.github.rfoltyns.vertx.grpc.MeasureRequest result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    public com.github.rfoltyns.vertx.grpc.MeasureRequest buildPartial() {
      com.github.rfoltyns.vertx.grpc.MeasureRequest result = new com.github.rfoltyns.vertx.grpc.MeasureRequest(this);
      int from_bitField0_ = bitField0_;
      int to_bitField0_ = 0;
      result.id_ = id_;
      result.consumer_ = consumer_;
      result.createdAt_ = createdAt_;
      result.receivedAt_ = receivedAt_;
      result.processedAt_ = processedAt_;
      result.delayMillis_ = delayMillis_;
      result.delayDeviation_ = delayDeviation_;
      result.numberOfHops_ = numberOfHops_;
      result.data_ = data_;
      result.sampleMap_ = internalGetSampleMap();
      result.sampleMap_.makeImmutable();
      result.bitField0_ = to_bitField0_;
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
      if (other instanceof com.github.rfoltyns.vertx.grpc.MeasureRequest) {
        return mergeFrom((com.github.rfoltyns.vertx.grpc.MeasureRequest)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.github.rfoltyns.vertx.grpc.MeasureRequest other) {
      if (other == com.github.rfoltyns.vertx.grpc.MeasureRequest.getDefaultInstance()) return this;
      if (other.getId() != 0) {
        setId(other.getId());
      }
      if (!other.getConsumer().isEmpty()) {
        consumer_ = other.consumer_;
        onChanged();
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
      if (other.getDelayMillis() != 0) {
        setDelayMillis(other.getDelayMillis());
      }
      if (other.getDelayDeviation() != 0) {
        setDelayDeviation(other.getDelayDeviation());
      }
      if (other.getNumberOfHops() != 0) {
        setNumberOfHops(other.getNumberOfHops());
      }
      if (other.getData() != com.google.protobuf.ByteString.EMPTY) {
        setData(other.getData());
      }
      internalGetMutableSampleMap().mergeFrom(
          other.internalGetSampleMap());
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
      com.github.rfoltyns.vertx.grpc.MeasureRequest parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (com.github.rfoltyns.vertx.grpc.MeasureRequest) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int bitField0_;

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

    private java.lang.Object consumer_ = "";
    /**
     * <code>string consumer = 2;</code>
     */
    public java.lang.String getConsumer() {
      java.lang.Object ref = consumer_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        consumer_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string consumer = 2;</code>
     */
    public com.google.protobuf.ByteString
        getConsumerBytes() {
      java.lang.Object ref = consumer_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        consumer_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string consumer = 2;</code>
     */
    public Builder setConsumer(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      consumer_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string consumer = 2;</code>
     */
    public Builder clearConsumer() {
      
      consumer_ = getDefaultInstance().getConsumer();
      onChanged();
      return this;
    }
    /**
     * <code>string consumer = 2;</code>
     */
    public Builder setConsumerBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      consumer_ = value;
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

    private int delayMillis_ ;
    /**
     * <code>int32 delayMillis = 6;</code>
     */
    public int getDelayMillis() {
      return delayMillis_;
    }
    /**
     * <code>int32 delayMillis = 6;</code>
     */
    public Builder setDelayMillis(int value) {
      
      delayMillis_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>int32 delayMillis = 6;</code>
     */
    public Builder clearDelayMillis() {
      
      delayMillis_ = 0;
      onChanged();
      return this;
    }

    private int delayDeviation_ ;
    /**
     * <code>int32 delayDeviation = 7;</code>
     */
    public int getDelayDeviation() {
      return delayDeviation_;
    }
    /**
     * <code>int32 delayDeviation = 7;</code>
     */
    public Builder setDelayDeviation(int value) {
      
      delayDeviation_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>int32 delayDeviation = 7;</code>
     */
    public Builder clearDelayDeviation() {
      
      delayDeviation_ = 0;
      onChanged();
      return this;
    }

    private int numberOfHops_ ;
    /**
     * <code>int32 numberOfHops = 8;</code>
     */
    public int getNumberOfHops() {
      return numberOfHops_;
    }
    /**
     * <code>int32 numberOfHops = 8;</code>
     */
    public Builder setNumberOfHops(int value) {
      
      numberOfHops_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>int32 numberOfHops = 8;</code>
     */
    public Builder clearNumberOfHops() {
      
      numberOfHops_ = 0;
      onChanged();
      return this;
    }

    private com.google.protobuf.ByteString data_ = com.google.protobuf.ByteString.EMPTY;
    /**
     * <code>bytes data = 9;</code>
     */
    public com.google.protobuf.ByteString getData() {
      return data_;
    }
    /**
     * <code>bytes data = 9;</code>
     */
    public Builder setData(com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      data_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>bytes data = 9;</code>
     */
    public Builder clearData() {
      
      data_ = getDefaultInstance().getData();
      onChanged();
      return this;
    }

    private com.google.protobuf.MapField<
        java.lang.String, java.lang.String> sampleMap_;
    private com.google.protobuf.MapField<java.lang.String, java.lang.String>
    internalGetSampleMap() {
      if (sampleMap_ == null) {
        return com.google.protobuf.MapField.emptyMapField(
            SampleMapDefaultEntryHolder.defaultEntry);
      }
      return sampleMap_;
    }
    private com.google.protobuf.MapField<java.lang.String, java.lang.String>
    internalGetMutableSampleMap() {
      onChanged();;
      if (sampleMap_ == null) {
        sampleMap_ = com.google.protobuf.MapField.newMapField(
            SampleMapDefaultEntryHolder.defaultEntry);
      }
      if (!sampleMap_.isMutable()) {
        sampleMap_ = sampleMap_.copy();
      }
      return sampleMap_;
    }

    public int getSampleMapCount() {
      return internalGetSampleMap().getMap().size();
    }
    /**
     * <code>map&lt;string, string&gt; sampleMap = 10;</code>
     */

    public boolean containsSampleMap(
        java.lang.String key) {
      if (key == null) { throw new java.lang.NullPointerException(); }
      return internalGetSampleMap().getMap().containsKey(key);
    }
    /**
     * Use {@link #getSampleMapMap()} instead.
     */
    @java.lang.Deprecated
    public java.util.Map<java.lang.String, java.lang.String> getSampleMap() {
      return getSampleMapMap();
    }
    /**
     * <code>map&lt;string, string&gt; sampleMap = 10;</code>
     */

    public java.util.Map<java.lang.String, java.lang.String> getSampleMapMap() {
      return internalGetSampleMap().getMap();
    }
    /**
     * <code>map&lt;string, string&gt; sampleMap = 10;</code>
     */

    public java.lang.String getSampleMapOrDefault(
        java.lang.String key,
        java.lang.String defaultValue) {
      if (key == null) { throw new java.lang.NullPointerException(); }
      java.util.Map<java.lang.String, java.lang.String> map =
          internalGetSampleMap().getMap();
      return map.containsKey(key) ? map.get(key) : defaultValue;
    }
    /**
     * <code>map&lt;string, string&gt; sampleMap = 10;</code>
     */

    public java.lang.String getSampleMapOrThrow(
        java.lang.String key) {
      if (key == null) { throw new java.lang.NullPointerException(); }
      java.util.Map<java.lang.String, java.lang.String> map =
          internalGetSampleMap().getMap();
      if (!map.containsKey(key)) {
        throw new java.lang.IllegalArgumentException();
      }
      return map.get(key);
    }

    public Builder clearSampleMap() {
      internalGetMutableSampleMap().getMutableMap()
          .clear();
      return this;
    }
    /**
     * <code>map&lt;string, string&gt; sampleMap = 10;</code>
     */

    public Builder removeSampleMap(
        java.lang.String key) {
      if (key == null) { throw new java.lang.NullPointerException(); }
      internalGetMutableSampleMap().getMutableMap()
          .remove(key);
      return this;
    }
    /**
     * Use alternate mutation accessors instead.
     */
    @java.lang.Deprecated
    public java.util.Map<java.lang.String, java.lang.String>
    getMutableSampleMap() {
      return internalGetMutableSampleMap().getMutableMap();
    }
    /**
     * <code>map&lt;string, string&gt; sampleMap = 10;</code>
     */
    public Builder putSampleMap(
        java.lang.String key,
        java.lang.String value) {
      if (key == null) { throw new java.lang.NullPointerException(); }
      if (value == null) { throw new java.lang.NullPointerException(); }
      internalGetMutableSampleMap().getMutableMap()
          .put(key, value);
      return this;
    }
    /**
     * <code>map&lt;string, string&gt; sampleMap = 10;</code>
     */

    public Builder putAllSampleMap(
        java.util.Map<java.lang.String, java.lang.String> values) {
      internalGetMutableSampleMap().getMutableMap()
          .putAll(values);
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


    // @@protoc_insertion_point(builder_scope:com.github.rfoltyns.vertx.grpc.MeasureRequest)
  }

  // @@protoc_insertion_point(class_scope:com.github.rfoltyns.vertx.grpc.MeasureRequest)
  private static final com.github.rfoltyns.vertx.grpc.MeasureRequest DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.github.rfoltyns.vertx.grpc.MeasureRequest();
  }

  public static com.github.rfoltyns.vertx.grpc.MeasureRequest getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<MeasureRequest>
      PARSER = new com.google.protobuf.AbstractParser<MeasureRequest>() {
    public MeasureRequest parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
        return new MeasureRequest(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<MeasureRequest> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<MeasureRequest> getParserForType() {
    return PARSER;
  }

  public com.github.rfoltyns.vertx.grpc.MeasureRequest getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

