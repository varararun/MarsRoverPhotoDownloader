package com.avarghese.marsrover.client;

import com.avarghese.marsrover.domain.Images;
import com.avarghese.marsrover.domain.Photo;
import com.avarghese.marsrover.service.ImagePublisher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MarsRoverAPIClientTest {

	@Autowired
	ImagePublisher mockImagePublisher;

	@Autowired
	RestTemplate mockRestTemplate;

	private MarsRoverAPIClient client;

	@Before
	public void setUp() {
		reset(mockImagePublisher);
		reset(mockRestTemplate);
		client = new MarsRoverAPIClient("basePath", "apiKey");
	}

	@Test
	public void testGetPhotos_dryRun_verifyFileServiceNotInvoked() {
		client.imagePublisher = mockImagePublisher;
		client.restTemplate = mockRestTemplate;

		when(mockRestTemplate.getForObject(anyString(), any())).thenReturn(buildMockPhotosObject());
		List<String> outputList = client.getPhotos("cameraType", "2018-01-01", 1, true);

		verify(mockRestTemplate, times(1)).getForObject(anyString(), any());
		verify(mockImagePublisher, times(0)).publishImage(anyString(), anyString(), anyString());

		assertEquals(3, outputList.size());
	}

	@Test
	public void testGetPhotos_verifyFileServiceInvoked() {
		client.imagePublisher = mockImagePublisher;
		client.restTemplate = mockRestTemplate;

		when(mockRestTemplate.getForObject(anyString(), any())).thenReturn(buildMockPhotosObject());
		List<String> outputList = client.getPhotos("cameraType", "2018-01-01", 100, false);

		verify(mockRestTemplate, times(1)).getForObject(anyString(), any());
		verify(mockImagePublisher, times(3)).publishImage(anyString(), anyString(), anyString());

		assertEquals(3, outputList.size());
	}

	@Test
	public void testGetPhotos_verifyFileServiceInvoked_limitReached() {
		client.imagePublisher = mockImagePublisher;
		client.restTemplate = mockRestTemplate;

		Images images = buildMockPhotosObject();
		Photo photo = images.getPhotos().get(0);
		when(mockRestTemplate.getForObject(anyString(), any())).thenReturn(buildMockPhotosObject());
		List<String> outputList = client.getPhotos("cameraType", "2018-01-01", 2, false);

		verify(mockRestTemplate, times(1)).getForObject(anyString(), any());
		verify(mockImagePublisher, times(2)).publishImage(anyString(), anyString(), anyString());

		assertEquals(3, outputList.size());
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
