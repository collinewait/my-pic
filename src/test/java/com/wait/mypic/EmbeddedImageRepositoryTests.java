package com.wait.mypic;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.junit4.SpringRunner;

import com.wait.mypic.images.Image;
import com.wait.mypic.images.ImageRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * @author collinewaitire 14 Sep 2019
 */
@RunWith(SpringRunner.class)
@DataMongoTest
public class EmbeddedImageRepositoryTests {

	@Autowired
	ImageRepository repository;

	@Autowired
	MongoOperations operations;

	@Before
	public void setUp() {
		operations.dropCollection(Image.class);
		operations.insert(new Image("1", "test-image1.jpg"));
		operations.insert(new Image("2", "test-image2.jpg"));
		operations.insert(new Image("3", "test-image3.jpg"));

		operations.findAll(Image.class).forEach(image -> {
			System.out.println(image.toString());
		});
	}

	// we would have not tested findAll since it is a framework code but I did it
	// just for learning purposes
	@Test
	public void findAllShouldWork() {
		Flux<Image> images = repository.findAll();
		StepVerifier.create(images).recordWith(ArrayList::new).expectNextCount(3) // Subscribe
																																							// to
																																							// Flux
																																							// with
																																							// StepVerifier
				.consumeRecordedWith(results -> {
					assertThat(results).hasSize(3);
					assertThat(results).extracting(Image::getName).contains(
							"test-image1.jpg", "test-image2.jpg", "test-image3.jpg");
				}).expectComplete().verify();
	}

	@Test
	public void findByNameShouldWork() {
		Mono<Image> image = repository.findByName("test-image1.jpg");
		StepVerifier.create(image).expectNextMatches(results -> {
			assertThat(results.getName()).isEqualTo("test-image1.jpg");
			assertThat(results.getId()).isEqualTo("1");
			// due to the functional nature of StepVerifier, we need to return a
			// boolean representing pass/fail
			return true;
		});
	}
}
