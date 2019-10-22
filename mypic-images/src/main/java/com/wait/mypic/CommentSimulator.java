package com.wait.mypic;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.validation.support.BindingAwareModelMap;

import com.wait.mypic.images.Comment;
import com.wait.mypic.images.CommentController;
import com.wait.mypic.images.ImageRepository;

import reactor.core.publisher.Mono;

/**
 * @author collinewaitire 26 Sep 2019
 */
@Profile("simulator")
@Component
public class CommentSimulator {

	private final HomeController homeController;
	private final CommentController commentController;
	private final ImageRepository repository;

	private final AtomicInteger counter;

	public CommentSimulator(HomeController homeController, CommentController commentController,
			ImageRepository repository) {
		this.homeController = homeController;
		this.commentController = commentController;
		this.repository = repository;
		this.counter = new AtomicInteger(1);
	}

	@Scheduled(fixedRate = 100)
	public void simulateActivity() {
		repository.findAll().map(image -> {
			Comment comment = new Comment();
			comment.setImageId(image.getId());
			comment.setComment("Comment #" + counter.getAndIncrement());
			return Mono.just(comment);
		}).map(commentController::addComment).subscribe();
	}

	@Scheduled(fixedRate = 500)
	public void simulateUsersClicking() {
		homeController.index(new BindingAwareModelMap(), null);
	}
}