package com.avarghese.marsrover.service;

import com.avarghese.marsrover.domain.Photo;

import java.util.List;

public interface PhotoPublisher {

	public List<String> publish(List<Photo> photos);
}
