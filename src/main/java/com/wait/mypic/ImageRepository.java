package com.wait.mypic;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Mono;

/**
 * @author collinewaitire 13 Sep 2019
 */
public interface ImageRepository extends ReactiveCrudRepository<Image, String> {
	Mono<Image> findByName(String name);
}
