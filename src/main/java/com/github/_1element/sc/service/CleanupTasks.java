package com.github._1element.sc.service; //NOSONAR

import com.github._1element.sc.domain.SurveillanceImage;
import com.github._1element.sc.properties.ImageProperties;
import com.github._1element.sc.repository.SurveillanceImageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Scheduler component to perform clean up tasks.
 */
@Component
public class CleanupTasks {

  private final SurveillanceImageRepository imageRepository;

  private final FileService fileService;

  private final ImageProperties imageProperties;

  @Value("${sc.archive.cleanup.enabled:false}")
  private Boolean isCleanupEnabled;

  @Value("${sc.archive.cleanup.keep:72}")
  private Integer keepHours;

  private static final String THUMBNAIL_PREFIX = "thumbnail.";

  private static final String CRON_EVERY_DAY_AT_4_AM = "0 0 4 * * *";

  private static final Logger LOG = LoggerFactory.getLogger(CleanupTasks.class);

  /**
   * Constructor.
   *
   * @param imageRepository the surveillance image repository
   * @param fileService the file service dependency
   * @param imageProperties the configured image properties
   */
  @Autowired
  public CleanupTasks(final SurveillanceImageRepository imageRepository, final FileService fileService,
                      final ImageProperties imageProperties) {
    this.imageRepository = imageRepository;
    this.fileService = fileService;
    this.imageProperties = imageProperties;
  }

  /**
   * Remove archived images older than X hours.
   */
  @Scheduled(cron = CRON_EVERY_DAY_AT_4_AM)
  public void cleanupArchive() {
    if (!Boolean.TRUE.equals(isCleanupEnabled)) {
      LOG.info("Task to remove old archived images not enabled in configuration. Do nothing.");
      return;
    }

    final LocalDateTime removeBefore = LocalDateTime.now().minusHours(keepHours);
    final List<SurveillanceImage> images = imageRepository.getArchivedImagesToCleanup(removeBefore);

    int numberOfImages = 0;
    for (final SurveillanceImage image : images) {
      final Path imageFilePath = fileService.getPath(imageProperties.getStorageDir() + image.getFileName());
      final Path thumbnailFilePath = fileService.getPath(imageProperties.getStorageDir() + THUMBNAIL_PREFIX
          + image.getFileName());
      try {
        fileService.delete(imageFilePath);
        fileService.delete(thumbnailFilePath);
        numberOfImages++;
      } catch (final IOException exception) {
        LOG.warn("Exception occurred while removing old archived image/thumbnail '{}'/'{}', cause '{}'",
            imageFilePath.toString(), thumbnailFilePath.toString(), exception.getMessage());
      }
      imageRepository.delete(image);
    }

    LOG.info("Successfully removed {} archived images.", numberOfImages);
  }

}
