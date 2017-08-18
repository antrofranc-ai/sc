package com.github._1element.sc.properties; //NOSONAR

import org.apache.commons.io.FileUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for WebDAV remote copy.
 */
@Component
@ConfigurationProperties("sc.remotecopy.webdav")
public class WebdavRemoteCopyProperties {

  private boolean enabled;
  
  private String username;
  
  private String password;
  
  private String host;
  
  private String dir = "/";
  
  private boolean cleanupEnabled = false;
  
  private long cleanupMaxDiskSpace;

  private int cleanupKeep;

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public String getDir() {
    return dir;
  }

  public void setDir(String dir) {
    this.dir = dir;
  }

  public boolean isCleanupEnabled() {
    return cleanupEnabled;
  }

  public void setCleanupEnabled(boolean cleanupEnabled) {
    this.cleanupEnabled = cleanupEnabled;
  }

  public long getCleanupMaxDiskSpace() {
    return cleanupMaxDiskSpace;
  }

  public void setCleanupMaxDiskSpace(long cleanupMaxDiskSpace) {
    this.cleanupMaxDiskSpace = FileUtils.ONE_MB * cleanupMaxDiskSpace;
  }

  public int getCleanupKeep() {
    return cleanupKeep;
  }

  public void setCleanupKeep(int cleanupKeep) {
    this.cleanupKeep = cleanupKeep;
  }
  
}
