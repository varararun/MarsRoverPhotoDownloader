package com.avarghese.marsrover.service;

import com.avarghese.marsrover.domain.Photo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ThreadedLocalPhotoPublisherTest {

	private ThreadedLocalPhotoPublisher photoPublisher;

	@Autowired
	ApplicationContext mockContext;

	@Before
	public void setUp() {

	}

	@Test
	public void testFileService() throws MalformedURLException {
		photoPublisher = new ThreadedLocalPhotoPublisher();
		photoPublisher.downloadsDirectory = "./downloads/";
		photoPublisher.context = mockContext;

		File testFile = new File("./src/test/resources/test.jpg");
		File savedFile = new File("./downloads/test/test.jpg");
		List<Photo> photos = new ArrayList<>();
		Photo photo = new Photo();
		photo.setImgSrc(testFile.toURI().toURL().toString());
		photo.setEarthDate("test");
		photos.add(photo);
		if (savedFile.exists()) {
			savedFile.delete();
		}
		assertEquals(false, savedFile.exists());
		photoPublisher.publish(photos);
		assertEquals(true, savedFile.exists());
		savedFile.delete();
		savedFile.getParentFile().delete();
	}
}
