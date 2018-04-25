package com.avarghese.marsrover.config;

import com.avarghese.marsrover.client.MarsRoverAPIClient;
import com.avarghese.marsrover.service.FlatFileImagePublisher;
import com.avarghese.marsrover.service.ImagePublisher;
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
	ImagePublisher mockImagePublisher;

	@MockBean
	RestTemplate mockRestTemplate;
}
