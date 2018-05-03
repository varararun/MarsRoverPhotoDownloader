package com.avarghese.marsrover.config;

import com.avarghese.marsrover.client.MarsRoverAPIClient;
import com.avarghese.marsrover.service.PhotoPublisher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableAutoConfiguration()
public class TestContextConfig {

	@MockBean
	MarsRoverAPIClient mockClient;

	@MockBean
	@Qualifier(value = "threadedLocalPhotoPublisher")
	PhotoPublisher mockPhotoPublisher;

	@MockBean
	RestTemplate mockRestTemplate;

}
