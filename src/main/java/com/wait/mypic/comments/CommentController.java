package com.wait.mypic.comments;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import reactor.core.publisher.Mono;

/**
 * @author collinewaitire 25 Sep 2019
 */
@Controller
public class CommentController {

	private final RabbitTemplate rabbitTemplate;

	public CommentController(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	@PostMapping("/comments")
	public Mono<String> addComment(Mono<Comment> newComment) {
		
		return newComment.flatMap(comment -> Mono.fromRunnable(() -> rabbitTemplate
				// the comment is published to RabbitMQ's my-pic
				// exchange with a routing key of comments.new
				.convertAndSend("my-pic", "comments.new", comment)))
				.log("commentService-publish").then(Mono.just("redirect:/"));
	}
}
