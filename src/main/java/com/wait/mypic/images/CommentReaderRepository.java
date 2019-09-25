package com.wait.mypic.images;

import org.springframework.data.repository.Repository;

import reactor.core.publisher.Flux;

/**
 * @author collinewaitire 25 Sep 2019
 */
public interface CommentReaderRepository extends Repository<Comment, String> {
	Flux<Comment> findByImageId(String imageId);
}
