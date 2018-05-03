package com.avarghese.marsrover.client;

import com.avarghese.marsrover.domain.Images;
import com.avarghese.marsrover.domain.Photo;
import com.avarghese.marsrover.service.PhotoPublisher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MarsRoverAPIClientTest {

	@Autowired
	@Qualifier(value = "threadedLocalPhotoPublisher")
	PhotoPublisher mockPhotoPublisher;

	@Autowired
	RestTemplate mockRestTemplate;

	private MarsRoverAPIClient client;

	@Before
	public void setUp() {
		reset(mockPhotoPublisher);
		reset(mockRestTemplate);
		client = new MarsRoverAPIClient("basePath", "apiKey");
	}

	@Test
	public void testGetPhotos_verifyFileServiceInvoked() {
		client.photoPublisher = mockPhotoPublisher;
		client.restTemplate = mockRestTemplate;
		Images images = buildMockPhotosObject();

		when(mockRestTemplate.getForObject(anyString(), any())).thenReturn(images);
		client.getPhotos("cameraType", "2018-01-01");

		verify(mockRestTemplate, times(1)).getForObject(anyString(), any());
		verify(mockPhotoPublisher, times(1)).publish(any());
	}

	@Test
	public void testGetPhotos_verifyFileServiceNotInvoked_NoImagesFoundOutput() {
		client.photoPublisher = mockPhotoPublisher;
		client.restTemplate = mockRestTemplate;
		Images images = buildMockPhotosObject();
		images.getPhotos().clear();

		when(mockRestTemplate.getForObject(anyString(), any())).thenReturn(images);
		List<String> output = client.getPhotos("cameraType", "2018-01-01");

		verify(mockRestTemplate, times(1)).getForObject(anyString(), any());
		verify(mockPhotoPublisher, times(0)).publish(any());

		assertEquals("No image(s) found", output.get(0));
	}

	private Images buildMockPhotosObject() {
		Images images = new Images();
		List<Photo> photoList = new ArrayList<>();
		photoList.add(buildPhoto(0));
		photoList.add(buildPhoto(1));
		photoList.add(buildPhoto(2));
		images.setPhotos(photoList);
		return images;
	}

	private Photo buildPhoto(int seed) {
		Photo photo = new Photo();
		photo.setEarthDate("2018-01-01");
		photo.setImgSrc("imgSrc-" + seed + ".jpg");
		return photo;
	}
}
