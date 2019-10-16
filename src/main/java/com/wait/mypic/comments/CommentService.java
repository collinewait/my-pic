package com.wait.mypic.comments;

import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import io.micrometer.core.instrument.MeterRegistry;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author collinewaitire 25 Sep 2019
 */
@Service
@EnableBinding(CustomProcessor.class)
public class CommentService {

	private CommentWritterRepository repository;
	private final MeterRegistry meterRegistry;

	public CommentService(CommentWritterRepository repository,
			MeterRegistry meterRegistry) {
		this.repository = repository;
		this.meterRegistry = meterRegistry;
	}

	@StreamListener
	@Output(CustomProcessor.OUTPUT)
	public Flux<Void> save(
			@Input(CustomProcessor.INPUT) Flux<Comment> newComments) {
		return repository.saveAll(newComments).flatMap(comment -> {
			meterRegistry
					.counter("comments.consumed", "imageId", comment.getImageId())
					.increment();
			return Mono.empty();
		});
	}

	@Bean
	CommandLineRunner setUp1(CommentWritterRepository repository) {
		return args -> {
			repository.deleteAll().subscribe();
		};
	}
}
