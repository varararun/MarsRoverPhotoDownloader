package com.avarghese.marsrover.controller;

import com.avarghese.marsrover.client.MarsRoverAPIClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class MarsRoverPhotosAPIController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	MarsRoverAPIClient client;

	@GetMapping("/api/v1/photos/{date}")
	public List<String> getMarsRoverPhotos(@PathVariable("date") String date,
	                                       @RequestParam(name = "camera", defaultValue = "") String camera) {
		log.info("Executing REST call with parameters, date={}, camera={}, dryRun={}", date, camera);
		List<String> results = client.getPhotos(camera, date);
		return results;
	}
}
