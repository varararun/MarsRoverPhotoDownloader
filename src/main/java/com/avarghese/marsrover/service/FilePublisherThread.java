package com.avarghese.marsrover.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

@Component
@Scope("prototype")
public class FilePublisherThread implements Callable<String> {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private static final String FORWARD_SLASH = "/";

	private String downloadsDirectory;
	private String fileDirectory;
	private String imageUrl;

	public FilePublisherThread(String downloadsDirectory, String fileDirectory, String imageUrl) {
		this.downloadsDirectory = downloadsDirectory;
		this.fileDirectory = fileDirectory;
		this.imageUrl = imageUrl;
	}

	@Override
	public String call() throws Exception {
		String output;
		String fileName = getFileName();
		String path = buildPath(downloadsDirectory, fileDirectory);
		String filePath =  path + fileName;
		File fileToDownload = new File(filePath);
		if (fileToDownload.exists()) {
			output = "File has already been downloaded: " + filePath;
			log.info(output);
			return output;
		}

		try {
			Files.createDirectories(Paths.get(path));
		} catch (IOException ioException) {
			output = ioException.getMessage();
			log.error(output);
			return output;
		}

		try (
				InputStream is = new URL(imageUrl).openStream();
				OutputStream os = new FileOutputStream(filePath);
		) {
			byte[] b = new byte[2048];
			int length;
			while ((length = is.read(b)) != -1) {
				os.write(b, 0, length);
			}
			output = "Successfully saved " + imageUrl + " as " + filePath;
			log.info(output);
			return output;
		} catch (Exception e) {
			output = e.getMessage();
			log.error(output);
			return output;
		}
	}

	private String buildPath(String downloadDirectory, String fileDirectory) {
		return downloadDirectory + fileDirectory + FORWARD_SLASH;
	}

	private String getFileName() {
		int start = imageUrl.lastIndexOf('/') + 1;
		return imageUrl.substring(start, imageUrl.length());
	}

}
