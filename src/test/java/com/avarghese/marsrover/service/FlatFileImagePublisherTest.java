package com.avarghese.marsrover.service;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;

import static org.junit.Assert.assertEquals;

public class FlatFileImagePublisherTest {

	private FlatFileImagePublisher imagePublisher;

	@Before
	public void setUp() {
		imagePublisher = new FlatFileImagePublisher();
		imagePublisher.downloadsDirectory = "./downloads/";
	}

	@Test
	public void testFileService() throws MalformedURLException {
		File testFile = new File("./src/test/resources/test.jpg");
		File savedFile = new File("./downloads/test/test.jpg");
		if (savedFile.exists()) {
			savedFile.delete();
		}
		assertEquals(false, savedFile.exists());
		imagePublisher.publishImage(testFile.toURI().toURL().toString(), "test", "test.jpg");
		assertEquals(true, savedFile.exists());
		savedFile.delete();
		savedFile.getParentFile().delete();
	}
}
