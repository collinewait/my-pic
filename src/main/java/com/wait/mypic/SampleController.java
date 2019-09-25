package com.wait.mypic;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.wait.mypic.images.Image;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author collinewaitire 8 Sep 2019
 */
@RestController("/api/images")
public class SampleController {

	@GetMapping
	Flux<Image> images() {
		return Flux.just(new Image("1", "image1"), new Image("2", "image2"),
				new Image("3", "image3"));
	}

	@PostMapping
	Mono<Void> create(@RequestBody Flux<Image> images) {
		return images.map(image -> {
			System.out
					.println("We will save" + image + " to a reactive database soon!");
			return image;
		}).then();
	}
}
