package com.avarghese.marsrover.service;

import com.avarghese.marsrover.domain.Photo;
import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class LocalPhotoPublisherTest {

	private LocalPhotoPublisher imagePublisher;

	@Test
	public void testFileService() throws MalformedURLException {
		imagePublisher = new LocalPhotoPublisher();
		imagePublisher.downloadsDirectory = "./downloads/";

		List<String> output;

		File testFile = new File("./src/test/resources/test.jpg");
		File savedFile = new File("./downloads/test/test.jpg");
		if (savedFile.exists()) {
			savedFile.delete();
		}

		List<Photo> photos = new ArrayList<>();
		Photo photo = new Photo();
		photo.setImgSrc(testFile.toURI().toURL().toString());
		photo.setEarthDate("test");
		photos.add(photo);

		assertEquals(false, savedFile.exists());
		imagePublisher.publish(photos);
		assertEquals(true, savedFile.exists());

		output = imagePublisher.publish(photos);
		assertEquals(true, savedFile.exists());
		assertEquals("File has already been downloaded: test.jpg", output.get(0));

		savedFile.delete();
		savedFile.getParentFile().delete();
	}
}
