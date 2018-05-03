package com.avarghese.marsrover.service;

import com.avarghese.marsrover.domain.Photo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Service
public class ThreadedLocalPhotoPublisher implements PhotoPublisher {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${download.directory}")
	protected String downloadsDirectory;

	@Autowired
	ApplicationContext context;

	@Override
	public List<String> publish(List<Photo> photos) {
		log.info("Using threaded photo publisher");
		String fileDirectory = photos.get(0).getEarthDate();
		List<String> outputList = new ArrayList<>();

		ExecutorService executorService = Executors.newFixedThreadPool(photos.size());
		List<FilePublisherThread> tasks = new ArrayList<>(photos.size());

		for (int i = 0; i < photos.size(); i++) {
			Photo photo = photos.get(i);
			String imageUrl = photo.getImgSrc();
			FilePublisherThread thread = context.getBean(FilePublisherThread.class, String.valueOf(i));
			thread.setParameters(downloadsDirectory, fileDirectory, imageUrl);
			tasks.add(thread);
		}
		try {
			List<Future<String>> futures = executorService.invokeAll(tasks);
			for (Future<String> future : futures) {
				outputList.add(future.get(60, TimeUnit.SECONDS));
			}
		} catch (Exception e) {
			outputList.add("Exception downloading files");
		} finally {
			executorService.shutdown();
		}
		return outputList;
	}
}
