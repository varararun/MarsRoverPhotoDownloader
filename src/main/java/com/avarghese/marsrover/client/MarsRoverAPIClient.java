package com.avarghese.marsrover.client;

import com.avarghese.marsrover.domain.Images;
import com.avarghese.marsrover.domain.Photo;
import com.avarghese.marsrover.service.ImagePublisher;
import com.avarghese.marsrover.utils.MarsRoverAPIQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
	ImagePublisher imagePublisher;

	public MarsRoverAPIClient(String basePath, String apiKey) {
		init();
		this.basePath = basePath;
		this.apiKey = apiKey;
	}

	public List<String> getPhotos(String camera, String date, boolean isDryRun) {
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
				throw new Exception("Check parameters, no images found");
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
		outputList.add("Attempting to download " + imageCount + " image(s)");
		for (int i = 0; i < imageCount; i++) {
			Photo photo = images.getPhotos().get(i);
			String fileName = getFileName(photo.getImgSrc());
			if (isDryRun) {
				outputList.add("Dry run, skipping file download: " + fileName);
			} else {
				String output = imagePublisher.publishImage(photo.getImgSrc(), photo.getEarthDate(), fileName);
				outputList.add(output);
			}
		}
		long end = System.currentTimeMillis();
		outputList.add("Download execution time was " + (end - start) / 1000 + " second(s)");
		return outputList;
	}

	private String getFileName(String imgsrc) {
		int start = imgsrc.lastIndexOf('/') + 1;
		return imgsrc.substring(start, imgsrc.length());
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

