package com.wait.chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

/**
 * @author collinewaitire 19 Oct 2019
 */
/**
 * This service consumes the messages sent from spring cloud stream. However,
 * the destination for these messages is not another spring cloud destination.
 * Instead we pipe them into a websocket session.
 */
@Service
@EnableBinding
public class CommentService implements WebSocketHandler {
	private static final Logger log = LoggerFactory.getLogger(CommentService.class);

	// We need a Jackson ObjectMapper, and will get it from spring's container
	// through the constructor
	private ObjectMapper mapper;
	private Flux<Comment> flux;
	private FluxSink<Comment> webSocketCommentSink;

	/**
	 * Think of webSocketCommentSink as the object we append comments to, and flux
	 * being the consumer pulling them off on the other end(after the consumer
	 * subscribes)
	 */
	CommentService(ObjectMapper mapper) {
		this.mapper = mapper;
		this.flux = Flux.<Comment>create(emitter -> webSocketCommentSink = emitter, FluxSink.OverflowStrategy.IGNORE)
				.publish().autoConnect();
	}

	@StreamListener(Sink.INPUT)
	public void broadcast(Comment comment) {
		if (webSocketCommentSink != null) {
			log.info("Publishing " + comment.toString() + " to websocket...");
			webSocketCommentSink.next(comment);
		}
	}

	/**
	 * Push this flux of comments over a websocket session
	 */
	@Override
	public Mono<Void> handle(WebSocketSession session) {
		return session.send(this.flux.map(comment -> {
			try {
				return mapper.writeValueAsString(comment);
			} catch (JsonProcessingException e) {
				throw new RuntimeException(e);
			}
		}).log("encod-as-json").map(session::textMessage).log("Wrap-as-websocket-message")).log("publish-to-websocket");
	}
}
