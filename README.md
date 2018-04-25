[![Build Status](https://travis-ci.org/varararun/MarsRoverPhotoDownloader.svg?branch=master)](https://travis-ci.org/varararun/MarsRoverPhotoDownloader)
![Maintainability Status](https://sonarcloud.io/api/project_badges/measure?project=MarsRoverPhotoDownloader&metric=sqale_rating)
![Coverage Status](https://sonarcloud.io/api/project_badges/measure?project=MarsRoverPhotoDownloader&metric=coverage)
![Bugs Status](https://sonarcloud.io/api/project_badges/measure?project=MarsRoverPhotoDownloader&metric=bugs)
![Vulnerabilities Status](https://sonarcloud.io/api/project_badges/measure?project=MarsRoverPhotoDownloader&metric=vulnerabilities)

## Notes
<!--
0. to run sonar report:
 ./gradlew sonarqube   -Dsonar.organization=vararun-github   -Dsonar.host.url=https://sonarcloud.io   -Dsonar.login=b508158c5f2ad698df6785b9608b2906dc37628d -Dsonar.sources=src -Dsonar.java.binaries=build/classes -Dsonar.java.libraries=gradle/**/*.jar -Dsonar.test.inclusions=src/test/** -Dsonar.exclusions=src/test/**
 https://sonarcloud.io/dashboard?id=MarsRoverPhotoDownloader
-->
1. All photos are saved to relative `./downloads` directory
2. Photos are in a nested directory, divided by `earth_date` param, using the following format: `yyyy-mm-dd` 
3. Built with java 1.8
4. Currently integrated with Travis-CI and Sonar for CI and Code Analysis, respectively

## Solution

#### To Run UI
1. Configure properties in `~/src/main/resources/application.yml`
2. Run `$ ./gradlew bootRun` in terminal/cmd
3. Navigate to `localhost:8080` or check terminal/cmd for tomcat port info
4. Specify camera type, select ALL to download all images captured on that day
5. Specify date, currently String input
6. Specify number of images to download for that day, if number is larger than available images, it will be ignored
7. Specify if this is a dry run, if checked, no actual images will be downloaded
8. Submit and wait for results
9. Check download directory for images

#### To Run Tests
1. Run `$ ./gradlew test` in terminal/cmd

#### TODO - Future State
1. Enhancement to extract client and build service jar
2. Include command line runner implementation so the required params can be passed as command line arguments
3. Include performance tests and static code analysis 

## Challenge Details

Are you up to the Challenge?  Please take the following coding challenge and upload your solution to your github.

Please note we are not looking for an exhaustive design more something to indicate 
they know how to work with the things we use on a daily basis.   
This should work for both front end and back end.

Using the API described here (https://api.marsrover.gov/api.html) 
build a project in GitHub that calls the Mars Rover API and selects a picture on a given day.  
The attached .txt file will list the dates the images were captured.   
We want your application to download and store each image locally.

Acceptance Criteria
- Please send the link of the GitHub repo as the answer to the question
- Language choice is up to you
- We should be able to build it locally after you submit it
- Include relevant documentation
- Bonus points for Unit Test, Static Analysis and Performance test
