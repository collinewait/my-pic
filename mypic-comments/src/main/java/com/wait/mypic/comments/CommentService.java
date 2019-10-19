package com.wait.mypic.comments;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import io.micrometer.core.instrument.MeterRegistry;
import reactor.core.publisher.Flux;

/**
 * @author collinewaitire 25 Sep 2019
 */
@Service
@EnableBinding(Processor.class)
public class CommentService {

	private static final Logger log = LoggerFactory.getLogger(CommentService.class);

	private CommentRepository repository;
	private final MeterRegistry meterRegistry;

	public CommentService(CommentRepository repository, MeterRegistry meterRegistry) {
		this.repository = repository;
		this.meterRegistry = meterRegistry;
	}

	@StreamListener
	@Output(Processor.OUTPUT)
	public Flux<Comment> save(@Input(Processor.INPUT) Flux<Comment> newComments) {
		return repository.saveAll(newComments).map(comment -> {
			log.info("Saving new comment " + comment);
			meterRegistry.counter("comments.consumed", "imageId", comment.getImageId()).increment();
			return comment;
		});
	}

	@Bean
	CommandLineRunner setUp(CommentRepository repository) {
		return args -> {
			repository.deleteAll().subscribe();
		};
	}
}
