package com.wait.chat;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;

import reactor.core.publisher.Mono;

/**
 * @author collinewaitire 20 Oct 2019
 */
@Service
@EnableBinding(ChatServiceStreams.class)
public class InboundChatService implements WebSocketHandler {

	private final ChatServiceStreams chatServiceStreams;

	public InboundChatService(ChatServiceStreams chatServiceStreams) {
		this.chatServiceStreams = chatServiceStreams;
	}

	@Override
	public Mono<Void> handle(WebSocketSession session) {
		return session.receive().log("inbound-incomming-chat-message").map(WebSocketMessage::getPayloadAsText)
				.log("inbound-convert-to-text").map(s -> session.getId() + ": " + s).log("inbound-mark-with-session-id")
				.flatMap(this::broadcast).log("inboun-broadcast-to-broker").then();
	}

	public Mono<?> broadcast(String message) {
		return Mono.fromRunnable(() -> {
			chatServiceStreams.clientToBroker().send(MessageBuilder.withPayload(message).build());
		});
	}
}
