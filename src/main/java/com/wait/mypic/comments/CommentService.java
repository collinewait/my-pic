package com.wait.mypic.comments;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import io.micrometer.core.instrument.MeterRegistry;

/**
 * @author collinewaitire 25 Sep 2019
 */
@Service
public class CommentService {

	private CommentWritterRepository repository;
	private final MeterRegistry meterRegistry;

	public CommentService(CommentWritterRepository repository,
			MeterRegistry meterRegistry) {
		this.repository = repository;
		this.meterRegistry = meterRegistry;
	}

	@RabbitListener(bindings = @QueueBinding(value = @Queue, exchange = @Exchange(value = "my-pic"), key = "comments.new"))
	public void save(Comment newComment) {
		repository.save(newComment).log("CommentService-save")
				.subscribe(comment -> {
					meterRegistry
							.counter("comments.consumed", "imageId", comment.getImageId())
							.increment();
				});
	}

	@Bean
	Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	// for development purposes, let us start our application with a clean slate.
	// to be disabled in production with a dev profile or removed.
	@Bean
	CommandLineRunner setUp1(MongoOperations operations) {
		return args -> {
			operations.dropCollection(Comment.class);
		};
	}
}
