package com.github._1element.sc.controller; //NOSONAR

import com.github._1element.sc.domain.PushNotificationSetting;
import com.github._1element.sc.dto.ImagesCountResult;
import com.github._1element.sc.exception.ResourceNotFoundException;
import com.github._1element.sc.repository.PushNotificationSettingRepository;
import com.github._1element.sc.repository.SurveillanceImageRepository;
import com.github._1element.sc.utils.URIConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(URIConstants.API_ROOT)
public class SurveillanceApiController {

  private SurveillanceImageRepository imageRepository;

  private PushNotificationSettingRepository pushNotificationSettingRepository;

  @Autowired
  public SurveillanceApiController(SurveillanceImageRepository imageRepository,
                                   PushNotificationSettingRepository pushNotificationSettingRepository) {
    this.imageRepository = imageRepository;
    this.pushNotificationSettingRepository = pushNotificationSettingRepository;
  }

  @GetMapping(URIConstants.API_RECORDINGS_COUNT)
  public ImagesCountResult recordingsCount() throws Exception {
    Long imagesCount = imageRepository.countAllImages();

    return new ImagesCountResult(imagesCount);
  }

  @PutMapping(URIConstants.API_PUSH_NOTIFICATION_SETTINGS + "/{cameraId}")
  public PushNotificationSetting updatePushNotificationSetting(@PathVariable String cameraId,
                                                               @RequestBody PushNotificationSetting pushNotificationSetting) throws Exception {

    PushNotificationSetting updatePushNotificationSetting = pushNotificationSettingRepository.findByCameraId(cameraId);
    if (updatePushNotificationSetting == null) {
      throw new ResourceNotFoundException("Resource '" + cameraId + "' not found.");
    }

    updatePushNotificationSetting.setEnabled(pushNotificationSetting.isEnabled());

    return pushNotificationSettingRepository.save(updatePushNotificationSetting);
  }

}
