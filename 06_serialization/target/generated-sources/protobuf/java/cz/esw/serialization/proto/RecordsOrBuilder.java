// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: measurements.proto

package cz.esw.serialization.proto;

public interface RecordsOrBuilder extends
    // @@protoc_insertion_point(interface_extends:esw.Records)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>repeated double Download = 1;</code>
   * @return A list containing the download.
   */
  java.util.List<java.lang.Double> getDownloadList();
  /**
   * <code>repeated double Download = 1;</code>
   * @return The count of download.
   */
  int getDownloadCount();
  /**
   * <code>repeated double Download = 1;</code>
   * @param index The index of the element to return.
   * @return The download at the given index.
   */
  double getDownload(int index);

  /**
   * <code>repeated double Upload = 2;</code>
   * @return A list containing the upload.
   */
  java.util.List<java.lang.Double> getUploadList();
  /**
   * <code>repeated double Upload = 2;</code>
   * @return The count of upload.
   */
  int getUploadCount();
  /**
   * <code>repeated double Upload = 2;</code>
   * @param index The index of the element to return.
   * @return The upload at the given index.
   */
  double getUpload(int index);

  /**
   * <code>repeated double Ping = 3;</code>
   * @return A list containing the ping.
   */
  java.util.List<java.lang.Double> getPingList();
  /**
   * <code>repeated double Ping = 3;</code>
   * @return The count of ping.
   */
  int getPingCount();
  /**
   * <code>repeated double Ping = 3;</code>
   * @param index The index of the element to return.
   * @return The ping at the given index.
   */
  double getPing(int index);
}
