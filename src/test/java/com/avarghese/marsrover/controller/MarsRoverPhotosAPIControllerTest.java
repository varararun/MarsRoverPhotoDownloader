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
		ArgumentCaptor<Integer> limitCaptor = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<Boolean> dryRunCaptor = ArgumentCaptor.forClass(Boolean.class);

		when(mockClient.getPhotos(cameraCaptor.capture(), dateCaptor.capture(), limitCaptor.capture(), dryRunCaptor.capture())).thenReturn(new ArrayList<>());
		restTemplate.getForEntity("http://localhost:" + port + "/api/v1/photos/2017-01-01", List.class);
		assertEquals(dateCaptor.getValue(), "2017-01-01");
		assertEquals(cameraCaptor.getValue(), "");
		assertEquals(limitCaptor.getValue().intValue(), 100);
		assertEquals(dryRunCaptor.getValue(), false);
	}

	@Test
	public void testRestAPIParams() {
		assertNotNull(controller);

		ArgumentCaptor<String> dateCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<String> cameraCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Integer> limitCaptor = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<Boolean> dryRunCaptor = ArgumentCaptor.forClass(Boolean.class);

		when(mockClient.getPhotos(cameraCaptor.capture(), dateCaptor.capture(), limitCaptor.capture(), dryRunCaptor.capture())).thenReturn(new ArrayList<>());
		restTemplate.getForEntity("http://localhost:" + port + "/api/v1/photos/2017-01-01?limit=1&camera=FHAZ&dryRun=true", List.class);
		assertEquals(dateCaptor.getValue(), "2017-01-01");
		assertEquals(cameraCaptor.getValue(), "FHAZ");
		assertEquals(limitCaptor.getValue().intValue(), 1);
		assertEquals(dryRunCaptor.getValue(), true);
	}
}
