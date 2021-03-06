package com.github._1element.sc.service; //NOSONAR

import com.github._1element.sc.domain.Camera;
import com.github._1element.sc.domain.PushNotificationSetting;
import com.github._1element.sc.domain.pushnotification.PushNotificationClient;
import com.github._1element.sc.domain.pushnotification.PushNotificationClientFactory;
import com.github._1element.sc.dto.PushNotificationSettingResource;
import com.github._1element.sc.events.PushNotificationEvent;
import com.github._1element.sc.exception.PushNotificationClientException;
import com.github._1element.sc.properties.PushNotificationProperties;
import com.github._1element.sc.repository.CameraRepository;
import com.github._1element.sc.repository.PushNotificationSettingRepository;
import com.google.common.annotations.VisibleForTesting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.event.EventListener;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service to send push notifications and retrieve configuration settings.
 */
@Service
public class PushNotificationService {

  private final PushNotificationClientFactory pushNotificationClientFactory;

  private final PushNotificationSettingRepository pushNotificationSettingRepository;

  private final CameraRepository cameraRepository;

  private final PushNotificationProperties properties;

  private final MessageSource messageSource;

  private static final Map<String, Instant> lastPushNotification = new HashMap<>();

  private static final String MESSAGE_PROPERTIES_PUSH_TITLE = "push-notification.title";

  private static final String MESSAGE_PROPERTIES_PUSH_MESSAGE = "push-notification.message";

  private static final Logger LOG = LoggerFactory.getLogger(PushNotificationService.class);

  /**
   * Constructs a push notification service.
   *
   * @param pushNotificationClientFactory the factory to create a specific push notification client
   * @param pushNotificationSettingRepository the setting repository
   * @param cameraRepository the camera repository
   * @param properties the configured push notification specific properties
   * @param messageSource the message source for localization
   */
  @Autowired
  public PushNotificationService(final PushNotificationClientFactory pushNotificationClientFactory,
                                 final PushNotificationSettingRepository pushNotificationSettingRepository,
                                 final CameraRepository cameraRepository,
                                 final PushNotificationProperties properties,
                                 final MessageSource messageSource) {
    this.pushNotificationClientFactory = pushNotificationClientFactory;
    this.pushNotificationSettingRepository = pushNotificationSettingRepository;
    this.cameraRepository = cameraRepository;
    this.properties = properties;
    this.messageSource = messageSource;
  }

  /**
   * Send push message.
   *
   * @param title message title
   * @param text message text
   */
  public void sendMessage(final String title, final String text) {
    if (!properties.isEnabled()) {
      LOG.debug("Push notifications are disabled.");
      return;
    }

    final PushNotificationClient pushNotificationClient =
        pushNotificationClientFactory.getClient(properties.getAdapter());

    try {
      pushNotificationClient.sendMessage(title, text);
      LOG.debug("Push notification with title '{}' was sent.", title);
    } catch (final PushNotificationClientException exception) {
      LOG.error("Push notification with title '{}' was not sent. {}", title, exception.getMessage());
    }
  }

  /**
   * Handles push notification events.
   * Will perform checks if notification should be sent or not.
   *
   * @param pushNotificationEvent push notification event
   */
  @EventListener
  public void handlePushNotificationEvent(final PushNotificationEvent pushNotificationEvent) {
    // check if notifications are enabled for this camera
    final Camera camera = pushNotificationEvent.getCamera();
    final PushNotificationSetting setting = pushNotificationSettingRepository.findByCameraId(camera.getId());
    if (setting == null || (!setting.isEnabled())) {
      LOG.debug("Push notifications are disabled for camera '{}'.", camera.getId());
      return;
    }

    // check if rate limit is reached
    if (hasRateLimitReached(camera.getId())) {
      return;
    }

    // send push message
    final String title = messageSource.getMessage(MESSAGE_PROPERTIES_PUSH_TITLE, new Object[]{camera.getName()},
        LocaleContextHolder.getLocale());
    final String message = messageSource.getMessage(MESSAGE_PROPERTIES_PUSH_MESSAGE, new Object[]{camera.getName(),
        LocalDateTime.now().toString()}, LocaleContextHolder.getLocale());
    sendMessage(title, message);
  }

  /**
   * Retrieve push notification configuration for each camera.
   *
   * @return camera push notification setting
   */
  public List<PushNotificationSettingResource> getAllSettings() {
    final List<PushNotificationSettingResource> resourcesList = new ArrayList<>();

    for (final Camera camera : cameraRepository.findAll()) {
      final PushNotificationSetting setting = pushNotificationSettingRepository.findByCameraId(camera.getId());
      final boolean enabled = (setting != null) ? setting.isEnabled() : false;
      resourcesList.add(new PushNotificationSettingResource(camera.getId(), camera.getName(), enabled));
    }

    return resourcesList;
  }

  /**
   * Perform rate limitation (throttle).
   * Push notification should not be sent if the time period between this
   * and the last run is less than the configured group time.
   *
   * @param cameraId the camera id to perform check for
   * @return true if rate limit is reached
   */
  @VisibleForTesting
  synchronized boolean hasRateLimitReached(final String cameraId) {
    final Instant currentInstant = Instant.now();
    final Instant lastPushNotificationInstant = lastPushNotification.get(cameraId);
    if ((lastPushNotificationInstant != null) && (properties.getGroupTime() > 0)) {
      final long minutesBetween = ChronoUnit.MINUTES.between(lastPushNotificationInstant, currentInstant);
      if (minutesBetween < properties.getGroupTime()) {
        LOG.debug("Push notification was not sent. Last push notification for camera '{}' was {} minute(s) ago. "
            + "Group time is {} minute(s).", cameraId, minutesBetween, properties.getGroupTime());
        return true;
      }
    }

    // save timestamp of this run
    lastPushNotification.put(cameraId, currentInstant);

    return false;
  }

}
