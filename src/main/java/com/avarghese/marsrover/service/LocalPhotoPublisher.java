package com.avarghese.marsrover.service;

import com.avarghese.marsrover.domain.Photo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class LocalPhotoPublisher implements PhotoPublisher {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private static final String FORWARD_SLASH = "/";

	@Value("${download.directory}")
	protected String downloadsDirectory;

	@Override
	public List<String> publish(List<Photo> photos) {
		log.info("Using synchronous photo publisher");
		String fileDirectory = photos.get(0).getEarthDate();
		List<String> outputList = new ArrayList<>();
		String output;
		for (Photo photo : photos) {
			String fileName = getFileName(photo.getImgSrc());
			String path = buildPath(downloadsDirectory, fileDirectory);
			String filePath = path + fileName;
			File fileToDownload = new File(filePath);
			if (fileToDownload.exists()) {
				output = "File has already been downloaded: " + fileName;
				log.info(output);
				outputList.add(output);
				continue;
			}

			try {
				Files.createDirectories(Paths.get(path));
			} catch (IOException ioException) {
				output = ioException.getMessage();
				log.error(output);
				outputList.add(output);
				continue;
			}

			try (
					InputStream is = new URL(photo.getImgSrc()).openStream();
					OutputStream os = new FileOutputStream(filePath);
			) {
				byte[] b = new byte[2048];
				int length;
				while ((length = is.read(b)) != -1) {
					os.write(b, 0, length);
				}
				output = "Successfully saved " + photo.getImgSrc() + " as " + filePath;
				log.info(output);
				outputList.add(output);
			} catch (Exception e) {
				output = e.getMessage();
				log.error(output);
				outputList.add(output);
			}
		}

		return outputList;
	}

	private String buildPath(String downloadDirectory, String fileDirectory) {
		return downloadDirectory + fileDirectory + FORWARD_SLASH;
	}

	private String getFileName(String imageUrl) {
		int start = imageUrl.lastIndexOf('/') + 1;
		return imageUrl.substring(start, imageUrl.length());
	}
}
