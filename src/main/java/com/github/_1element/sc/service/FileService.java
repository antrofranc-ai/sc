package com.github._1element.sc.service; //NOSONAR

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github._1element.sc.properties.ImageProperties;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * File service class.
 */
@Service
public class FileService {

  private final ImageProperties imageProperties;

  private static final String DATE_TIME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS";

  private static final String SEPARATOR = "-";

  @Autowired
  public FileService(final ImageProperties imageProperties) {
    this.imageProperties = imageProperties;
  }

  /**
   * Returns true if file has valid extension.
   * If there are no valid extensions configured, this will always
   * evaluate to true.
   *
   * @param filename filename to check
   * @return true if extension is valid
   */
  public boolean hasValidExtension(final String filename) {
    if (imageProperties.getValidExtensions() == null) {
      return true;
    }

    for (final String validExtension : imageProperties.getValidExtensions()) {
      if (StringUtils.endsWithIgnoreCase(filename, validExtension)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Returns unique filename prefix.
   *
   * @return prefix
   */
  public String getUniquePrefix() {
    final String timestamp = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT).format(LocalDateTime.now());

    return timestamp + SEPARATOR + RandomStringUtils.randomAlphabetic(7);
  }

  /**
   * Creates a new {@link InputStream} for a given path.
   *
   * @param path the path to the file to open
   * @return a new input stream
   * @throws IOException IO exception in case of an error
   */
  public InputStream createInputStream(final Path path) throws IOException {
    return Files.newInputStream(path);
  }

  /**
   * Deletes a file.
   *
   * @param path the path to the file to delete
   * @throws IOException IO exception in case of an error
   */
  public void delete(final Path path) throws IOException {
    Files.delete(path);
  }

  /**
   * Converts a path string to a Path.
   * This is basically just a wrapper for {@link Paths#get(String, String...)} to simplify unit testing.
   *
   * @param first the path string or initial part of the path string
   * @param more additional strings to be joined to form the path string
   * @return the resulting Path
   */
  public Path getPath(final String first, final String... more) {
    return Paths.get(first, more);
  }

  /**
   * Moves a file.
   * This is just a wrapper for the static {@link Files#move(Path, Path, CopyOption...)} method
   * to simplify unit testing.
   *
   * @param sourcePath the source to be moved
   * @param destinationPath the destination path
   * @throws IOException IO exception in case of an error
   */
  public void moveFile(final Path sourcePath, final Path destinationPath) throws IOException {
    Files.move(sourcePath, destinationPath);
  }

  /**
   * Writes bytes to a file.
   * This is a wrapper for the static {@link Files#write(Path, byte[], OpenOption...)} method.
   *
   * @param path the path to the file
   * @param bytes the byte array to write
   * @return the path
   * @throws IOException if an IO error occurs
   */
  public Path write(final Path path, final byte[] bytes) throws IOException {
    return Files.write(path, bytes);
  }

  /**
   * Reads all the bytes from a file.
   * This is a wrapper for the static {@link Files#readAllBytes(Path)} method.
   *
   * @param path the path to the file
   * @return a byte array containing the bytes read from the file
   * @throws IOException if an IO error occurs
   */
  public byte[] readAllBytes(final Path path) throws IOException {
    return Files.readAllBytes(path);
  }

}
