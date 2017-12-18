package com.github._1element.sc.domain; //NOSONAR

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Surveillance camera POJO.
 */
public class Camera {

  private String id;

  private String name;

  private Integer rotation;

  @JsonIgnore
  private String host;

  @JsonIgnore
  private String ftpUsername;

  @JsonIgnore
  private String ftpPassword;

  @JsonIgnore
  private String ftpIncomingDirectory;

  private String snapshotUrl;

  private String streamUrl;

  /**
   * Constructs a new camera.
   *
   * @param id the unique id of the camera
   * @param name the camera name
   * @param rotation optional rotation property
   * @param host the (internal) host the camera is running on
   * @param ftpUsername the ftp username for incoming files
   * @param ftpPassword the ftp password for incoming files
   * @param ftpIncomingDirectory the ftp incoming directory
   * @param snapshotUrl optional url to retrieve snapshots
   * @param streamUrl optional url to the camera stream
   */
  public Camera(String id, String name, Integer rotation, String host, String ftpUsername, String ftpPassword,
      String ftpIncomingDirectory, String snapshotUrl, String streamUrl) {
    this.id = id;
    this.name = name;
    this.rotation = rotation;
    this.host = host;
    this.ftpUsername = ftpUsername;
    this.ftpPassword = ftpPassword;
    this.ftpIncomingDirectory = ftpIncomingDirectory;
    this.snapshotUrl = snapshotUrl;
    this.streamUrl = streamUrl;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Integer getRotation() {
    return rotation;
  }

  public String getHost() {
    return host;
  }

  public String getFtpUsername() {
    return ftpUsername;
  }

  public String getFtpPassword() {
    return ftpPassword;
  }

  public String getFtpIncomingDirectory() {
    return ftpIncomingDirectory;
  }

  public boolean hasSnapshotUrl() {
    return snapshotUrl != null;
  }

  public String getSnapshotUrl() {
    return snapshotUrl;
  }

  public boolean hasStreamUrl() {
    return streamUrl != null;
  }

  public String getStreamUrl() {
    return streamUrl;
  }

}
