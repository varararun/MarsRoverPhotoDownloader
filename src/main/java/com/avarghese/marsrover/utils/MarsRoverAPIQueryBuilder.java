package com.avarghese.marsrover.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MarsRoverAPIQueryBuilder {

	private final Log log = LogFactory.getLog(this.getClass());

	private static final String CAMERA_TYPE_PARAM = "camera=";
	private static final String DATE_PARAM = "earth_date=";
	private static final String API_KEY_PARAM = "api_key=";
	private static final String PAGE_PARAM = "page=";
	private static final String AMPERSAND = "&";
	private static final String QUESTION_MARK = "?";
	private static final int DEFAULT_PAGE = 0;

	private StringBuilder query;

	public MarsRoverAPIQueryBuilder() {
		query = new StringBuilder();
	}

	public MarsRoverAPIQueryBuilder withBasePath(String basePath) {
		query.append(basePath + QUESTION_MARK);
		return this;
	}

	public MarsRoverAPIQueryBuilder withCamera(String camera) {
		if (camera != null && camera.length() > 0) {
			query.append(CAMERA_TYPE_PARAM + camera + AMPERSAND);
		}
		return this;
	}

	public MarsRoverAPIQueryBuilder withDate(String date) {
		query.append(DATE_PARAM + date + AMPERSAND);
		return this;
	}

	public MarsRoverAPIQueryBuilder withApiKey(String apiKey) {
		query.append(API_KEY_PARAM + apiKey + AMPERSAND);
		return this;
	}

	public MarsRoverAPIQueryBuilder withPage(int page) {
		query.append(PAGE_PARAM + page + AMPERSAND);
		return this;
	}

	public String build() {
		log.info("URL: " + query);
		return query.toString();
	}

}