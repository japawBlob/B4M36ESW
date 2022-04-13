// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: measurements.proto

package cz.esw.serialization.proto;

/**
 * <pre>
 * ------------ Measurement info message according to measurementinfo.h ------------ 
 * </pre>
 *
 * Protobuf type {@code esw.MeasurementInfo}
 */
public final class MeasurementInfo extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:esw.MeasurementInfo)
    MeasurementInfoOrBuilder {
private static final long serialVersionUID = 0L;
  // Use MeasurementInfo.newBuilder() to construct.
  private MeasurementInfo(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private MeasurementInfo() {
    measurerName_ = "";
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new MeasurementInfo();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private MeasurementInfo(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new java.lang.NullPointerException();
    }
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          case 8: {

            id_ = input.readInt32();
            break;
          }
          case 16: {

            timestamp_ = input.readInt64();
            break;
          }
          case 26: {
            java.lang.String s = input.readStringRequireUtf8();

            measurerName_ = s;
            break;
          }
          default: {
            if (!parseUnknownField(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
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
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return cz.esw.serialization.proto.Measurements.internal_static_esw_MeasurementInfo_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return cz.esw.serialization.proto.Measurements.internal_static_esw_MeasurementInfo_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            cz.esw.serialization.proto.MeasurementInfo.class, cz.esw.serialization.proto.MeasurementInfo.Builder.class);
  }

  public static final int ID_FIELD_NUMBER = 1;
  private int id_;
  /**
   * <code>int32 id = 1;</code>
   * @return The id.
   */
  @java.lang.Override
  public int getId() {
    return id_;
  }

  public static final int TIMESTAMP_FIELD_NUMBER = 2;
  private long timestamp_;
  /**
   * <code>int64 timestamp = 2;</code>
   * @return The timestamp.
   */
  @java.lang.Override
  public long getTimestamp() {
    return timestamp_;
  }

  public static final int MEASURERNAME_FIELD_NUMBER = 3;
  private volatile java.lang.Object measurerName_;
  /**
   * <code>string measurerName = 3;</code>
   * @return The measurerName.
   */
  @java.lang.Override
  public java.lang.String getMeasurerName() {
    java.lang.Object ref = measurerName_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      measurerName_ = s;
      return s;
    }
  }
  /**
   * <code>string measurerName = 3;</code>
   * @return The bytes for measurerName.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getMeasurerNameBytes() {
    java.lang.Object ref = measurerName_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      measurerName_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (id_ != 0) {
      output.writeInt32(1, id_);
    }
    if (timestamp_ != 0L) {
      output.writeInt64(2, timestamp_);
    }
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(measurerName_)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 3, measurerName_);
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (id_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(1, id_);
    }
    if (timestamp_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt64Size(2, timestamp_);
    }
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(measurerName_)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(3, measurerName_);
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof cz.esw.serialization.proto.MeasurementInfo)) {
      return super.equals(obj);
    }
    cz.esw.serialization.proto.MeasurementInfo other = (cz.esw.serialization.proto.MeasurementInfo) obj;

    if (getId()
        != other.getId()) return false;
    if (getTimestamp()
        != other.getTimestamp()) return false;
    if (!getMeasurerName()
        .equals(other.getMeasurerName())) return false;
    if (!unknownFields.equals(other.unknownFields)) return false;
    return true;
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
    hash = (37 * hash) + TIMESTAMP_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getTimestamp());
    hash = (37 * hash) + MEASURERNAME_FIELD_NUMBER;
    hash = (53 * hash) + getMeasurerName().hashCode();
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static cz.esw.serialization.proto.MeasurementInfo parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static cz.esw.serialization.proto.MeasurementInfo parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static cz.esw.serialization.proto.MeasurementInfo parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static cz.esw.serialization.proto.MeasurementInfo parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static cz.esw.serialization.proto.MeasurementInfo parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static cz.esw.serialization.proto.MeasurementInfo parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static cz.esw.serialization.proto.MeasurementInfo parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static cz.esw.serialization.proto.MeasurementInfo parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static cz.esw.serialization.proto.MeasurementInfo parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static cz.esw.serialization.proto.MeasurementInfo parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static cz.esw.serialization.proto.MeasurementInfo parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static cz.esw.serialization.proto.MeasurementInfo parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(cz.esw.serialization.proto.MeasurementInfo prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
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
   * ------------ Measurement info message according to measurementinfo.h ------------ 
   * </pre>
   *
   * Protobuf type {@code esw.MeasurementInfo}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:esw.MeasurementInfo)
      cz.esw.serialization.proto.MeasurementInfoOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return cz.esw.serialization.proto.Measurements.internal_static_esw_MeasurementInfo_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return cz.esw.serialization.proto.Measurements.internal_static_esw_MeasurementInfo_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              cz.esw.serialization.proto.MeasurementInfo.class, cz.esw.serialization.proto.MeasurementInfo.Builder.class);
    }

    // Construct using cz.esw.serialization.proto.MeasurementInfo.newBuilder()
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
    @java.lang.Override
    public Builder clear() {
      super.clear();
      id_ = 0;

      timestamp_ = 0L;

      measurerName_ = "";

      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return cz.esw.serialization.proto.Measurements.internal_static_esw_MeasurementInfo_descriptor;
    }

    @java.lang.Override
    public cz.esw.serialization.proto.MeasurementInfo getDefaultInstanceForType() {
      return cz.esw.serialization.proto.MeasurementInfo.getDefaultInstance();
    }

    @java.lang.Override
    public cz.esw.serialization.proto.MeasurementInfo build() {
      cz.esw.serialization.proto.MeasurementInfo result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public cz.esw.serialization.proto.MeasurementInfo buildPartial() {
      cz.esw.serialization.proto.MeasurementInfo result = new cz.esw.serialization.proto.MeasurementInfo(this);
      result.id_ = id_;
      result.timestamp_ = timestamp_;
      result.measurerName_ = measurerName_;
      onBuilt();
      return result;
    }

    @java.lang.Override
    public Builder clone() {
      return super.clone();
    }
    @java.lang.Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.setField(field, value);
    }
    @java.lang.Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }
    @java.lang.Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }
    @java.lang.Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return super.setRepeatedField(field, index, value);
    }
    @java.lang.Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.addRepeatedField(field, value);
    }
    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof cz.esw.serialization.proto.MeasurementInfo) {
        return mergeFrom((cz.esw.serialization.proto.MeasurementInfo)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(cz.esw.serialization.proto.MeasurementInfo other) {
      if (other == cz.esw.serialization.proto.MeasurementInfo.getDefaultInstance()) return this;
      if (other.getId() != 0) {
        setId(other.getId());
      }
      if (other.getTimestamp() != 0L) {
        setTimestamp(other.getTimestamp());
      }
      if (!other.getMeasurerName().isEmpty()) {
        measurerName_ = other.measurerName_;
        onChanged();
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      cz.esw.serialization.proto.MeasurementInfo parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (cz.esw.serialization.proto.MeasurementInfo) e.getUnfinishedMessage();
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
     * @return The id.
     */
    @java.lang.Override
    public int getId() {
      return id_;
    }
    /**
     * <code>int32 id = 1;</code>
     * @param value The id to set.
     * @return This builder for chaining.
     */
    public Builder setId(int value) {
      
      id_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>int32 id = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearId() {
      
      id_ = 0;
      onChanged();
      return this;
    }

    private long timestamp_ ;
    /**
     * <code>int64 timestamp = 2;</code>
     * @return The timestamp.
     */
    @java.lang.Override
    public long getTimestamp() {
      return timestamp_;
    }
    /**
     * <code>int64 timestamp = 2;</code>
     * @param value The timestamp to set.
     * @return This builder for chaining.
     */
    public Builder setTimestamp(long value) {
      
      timestamp_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>int64 timestamp = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearTimestamp() {
      
      timestamp_ = 0L;
      onChanged();
      return this;
    }

    private java.lang.Object measurerName_ = "";
    /**
     * <code>string measurerName = 3;</code>
     * @return The measurerName.
     */
    public java.lang.String getMeasurerName() {
      java.lang.Object ref = measurerName_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        measurerName_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string measurerName = 3;</code>
     * @return The bytes for measurerName.
     */
    public com.google.protobuf.ByteString
        getMeasurerNameBytes() {
      java.lang.Object ref = measurerName_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        measurerName_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string measurerName = 3;</code>
     * @param value The measurerName to set.
     * @return This builder for chaining.
     */
    public Builder setMeasurerName(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      measurerName_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string measurerName = 3;</code>
     * @return This builder for chaining.
     */
    public Builder clearMeasurerName() {
      
      measurerName_ = getDefaultInstance().getMeasurerName();
      onChanged();
      return this;
    }
    /**
     * <code>string measurerName = 3;</code>
     * @param value The bytes for measurerName to set.
     * @return This builder for chaining.
     */
    public Builder setMeasurerNameBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      measurerName_ = value;
      onChanged();
      return this;
    }
    @java.lang.Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @java.lang.Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:esw.MeasurementInfo)
  }

  // @@protoc_insertion_point(class_scope:esw.MeasurementInfo)
  private static final cz.esw.serialization.proto.MeasurementInfo DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new cz.esw.serialization.proto.MeasurementInfo();
  }

  public static cz.esw.serialization.proto.MeasurementInfo getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<MeasurementInfo>
      PARSER = new com.google.protobuf.AbstractParser<MeasurementInfo>() {
    @java.lang.Override
    public MeasurementInfo parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new MeasurementInfo(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<MeasurementInfo> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<MeasurementInfo> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public cz.esw.serialization.proto.MeasurementInfo getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

