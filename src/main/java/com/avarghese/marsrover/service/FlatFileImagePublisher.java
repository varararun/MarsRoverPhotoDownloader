package com.avarghese.marsrover.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class FlatFileImagePublisher implements ImagePublisher {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private static final String FORWARD_SLASH = "/";

	@Value("${download.directory:'./downloads/'}")
	protected String downloadsDirectory;

	@Override
	public String publishImage(String imageUrl, String fileDirectory, String fileName) {
		String path = downloadsDirectory + fileDirectory + FORWARD_SLASH + fileName;
		String output;

		File fileToDownload = new File(path);
		if (fileToDownload.exists()) {
			output = "File has already been downloaded: " + fileName;
			log.info(output);
			return output;
		}

		try {
			Files.createDirectories(Paths.get(downloadsDirectory + fileDirectory));
		} catch (IOException ioException) {
			output = ioException.getMessage();
			log.error(output);
			return output;
		}

		try (
				InputStream is = new URL(imageUrl).openStream();
				OutputStream os = new FileOutputStream(path);
		) {
			byte[] b = new byte[2048];
			int length;
			while ((length = is.read(b)) != -1) {
				os.write(b, 0, length);
			}
			output = "Successfully saved " + imageUrl + "\n as " + path;
			log.info(output);
			return output;
		} catch (Exception e) {
			output = e.getMessage();
			log.error(output);
			return output;
		}
	}

}
