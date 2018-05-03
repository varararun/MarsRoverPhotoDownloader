package com.avarghese.marsrover.controller;

import com.avarghese.marsrover.client.MarsRoverAPIClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MarsRoverPhotosAPIControllerTest {

	@LocalServerPort
	private int port;

	@Autowired
	MarsRoverPhotosAPIController controller;

	@Autowired
	TestRestTemplate restTemplate;

	@Autowired
	MarsRoverAPIClient mockClient;

	@Before
	public void setUp() {
		reset(mockClient);
	}

	@Test
	public void testRestAPIDefaultParams() {
		assertNotNull(controller);

		ArgumentCaptor<String> dateCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<String> cameraCaptor = ArgumentCaptor.forClass(String.class);

		when(mockClient.getPhotos(cameraCaptor.capture(), dateCaptor.capture())).thenReturn(new ArrayList<>());
		restTemplate.getForEntity("http://localhost:" + port + "/api/v1/photos/2017-01-01", List.class);
		assertEquals("2017-01-01", dateCaptor.getValue());
		assertEquals("", cameraCaptor.getValue());
	}

	@Test
	public void testRestAPIParams() {
		assertNotNull(controller);

		ArgumentCaptor<String> dateCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<String> cameraCaptor = ArgumentCaptor.forClass(String.class);

		when(mockClient.getPhotos(cameraCaptor.capture(), dateCaptor.capture())).thenReturn(new ArrayList<>());
		restTemplate.getForEntity("http://localhost:" + port + "/api/v1/photos/2017-01-01?camera=FHAZ", List.class);
		assertEquals("2017-01-01", dateCaptor.getValue());
		assertEquals("FHAZ", cameraCaptor.getValue());
	}
}
