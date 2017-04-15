package com.github._1element.sc.repository;

import com.github._1element.sc.SurveillanceCenterApplication;
import com.github._1element.sc.domain.Camera;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SurveillanceCenterApplication.class)
@WebAppConfiguration
public class CameraRepositoryTest {

  @Autowired
  private CameraRepository cameraRepository;

  @Test
  public void testFindAll() throws Exception {
    List<Camera> result = cameraRepository.findAll();

    assertEquals(2, result.size());
  }

  @Test
  public void testFindById() throws Exception {
    Camera camera1 = cameraRepository.findById("testcamera1");

    assertEquals("Front door", camera1.getName());
    assertEquals((Integer)270, camera1.getRotation());
    assertEquals("192.168.1.50", camera1.getHost());
    assertEquals("username", camera1.getFtpUsername());
    assertEquals("password", camera1.getFtpPassword());
    assertEquals("https://localhost/cam1/snapshot", camera1.getSnapshotUrl());
    assertEquals("https://localhost/cam1/videostream", camera1.getStreamUrl());
    assertEquals("/tmp/camera1/", camera1.getFtpIncomingDirectory());

    Camera camera2 = cameraRepository.findById("testcamera2");

    assertEquals("Backyard", camera2.getName());
    assertNull(camera2.getRotation());
    assertEquals("192.168.1.51", camera2.getHost());
    assertEquals("user2", camera2.getFtpUsername());
    assertEquals("password2", camera2.getFtpPassword());
    assertEquals("https://localhost/cam2/snapshot", camera2.getSnapshotUrl());
    assertEquals("https://localhost/cam2/videostream", camera2.getStreamUrl());
    assertEquals("/tmp/camera2/", camera2.getFtpIncomingDirectory());
  }

  @Test
  public void testFindByFtpUsername() throws Exception {
    Camera camera1 = cameraRepository.findByFtpUsername("username");

    assertEquals("testcamera1", camera1.getId());
    assertEquals("password", camera1.getFtpPassword());
    assertEquals("/tmp/camera1/", camera1.getFtpIncomingDirectory());

    Camera camera2 = cameraRepository.findByFtpUsername("user2");

    assertEquals("testcamera2", camera2.getId());
    assertEquals("password2", camera2.getFtpPassword());
    assertEquals("/tmp/camera2/", camera2.getFtpIncomingDirectory());
  }

}
