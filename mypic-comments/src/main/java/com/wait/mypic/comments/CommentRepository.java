package com.wait.mypic.comments;

import org.springframework.data.repository.Repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author collinewaitire 25 Sep 2019
 */
/*
 * Spring Data requires a findOne() operation in order to perform saves because
 * that's what it uses to fetch what we just saved in order to return it. This
 * repository is focused on writing data into MongoDB and nothing more. Even
 * though it has a findOne(), it's not built for reading data. That has been
 * kept over in the images package.
 */
public interface CommentRepository extends Repository<Comment, String> {
	Flux<Comment> findByImageId(String imageId);

	Flux<Comment> saveAll(Flux<Comment> newComment);

	// Needed to support save()
	Mono<Comment> findById(String id);

	Mono<Void> deleteAll();
}
