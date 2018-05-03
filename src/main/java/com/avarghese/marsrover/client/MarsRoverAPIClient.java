package com.avarghese.marsrover.client;

import com.avarghese.marsrover.domain.Images;
import com.avarghese.marsrover.service.PhotoPublisher;
import com.avarghese.marsrover.utils.MarsRoverAPIQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MarsRoverAPIClient {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	protected String basePath;
	protected String apiKey;
	protected RestTemplate restTemplate;

	@Autowired
	@Qualifier(value = "threadedLocalPhotoPublisher")
	PhotoPublisher photoPublisher;

	public MarsRoverAPIClient(String basePath, String apiKey) {
		init();
		this.basePath = basePath;
		this.apiKey = apiKey;
	}

	public List<String> getPhotos(String camera, String date) {
		log.info("Retrieving and downloading photos using the following parameters, camera={}, date={}", camera, date);
		List<String> outputList = new ArrayList<>();
		int imageCount;
		Images images;
		String url = new MarsRoverAPIQueryBuilder()
				.withBasePath(basePath)
				.withApiKey(apiKey)
				.withCamera(camera)
				.withDate(date)
				.build();
		try {
			images = restTemplate.getForObject(url, Images.class);
			if (images == null || images.getPhotos() == null) {
				throw new IllegalArgumentException("Check parameters, no images found");
			}
			imageCount = images.getPhotos().size();
			if (imageCount == 0) {
				outputList.add("No image(s) found");
				return outputList;
			}
		} catch (Exception e) {
			outputList.add("Exception calling API: " + e.getMessage());
			log.error("Error calling API", e);
			return outputList;
		}
		long start = System.currentTimeMillis();
		log.info("Retrieved {} image(s)", imageCount);
		outputList = photoPublisher.publish(images.getPhotos());
		long end = System.currentTimeMillis();
		log.info("Download execution time was {} second(s)", ((end - start) / 1000));
		return outputList;
	}

	private void init() {
		restTemplate = new RestTemplate();
		restTemplate.setErrorHandler(new ResponseErrorHandler() {
			@Override
			public boolean hasError(ClientHttpResponse response) throws IOException {
				return !response.getStatusCode().is2xxSuccessful();
			}

			@Override
			public void handleError(ClientHttpResponse response) throws IOException {
				log.error("Error on API call: " + response.getBody());
			}
		});
	}
}

