package com.avarghese.marsrover.config;

import com.avarghese.marsrover.client.MarsRoverAPIClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MarsRoverClientConfig {

	@Value("${external.nasa.marsrover.api}")
	private String basePath;

	@Value("${external.nasa.marsrover.api.key}")
	private String apiKey;

	@Bean
	public MarsRoverAPIClient client() {
		return new MarsRoverAPIClient(basePath, apiKey);
	}
}
