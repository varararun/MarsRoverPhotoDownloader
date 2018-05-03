package com.avarghese.marsrover.controller;


import com.avarghese.marsrover.client.MarsRoverAPIClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HomeControllerTest {

	@LocalServerPort
	private int port;

	@Autowired
	HomeController controller;

	@Autowired
	MarsRoverAPIClient mockClient;

	@Autowired
	private TestRestTemplate restTemplate;

	@Before
	public void setUp() {
		reset(mockClient);
	}

	@Test
	public void testHomeController() {
		assertNotNull(controller);
		controller.client = mockClient;

		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/",
				String.class)).contains("Mars Rover Photo");

		this.restTemplate.postForLocation("http://localhost:" + port + "/", String.class);

		verify(mockClient, times(1)).getPhotos(anyString(), anyString());
	}
}
