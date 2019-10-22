package com.wait.chat;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

/**
 * @author collinewaitire 19 Oct 2019
 */
@Configuration
public class WebSocketConfig {

	@Bean
	HandlerMapping webSocketMapping(CommentService commentService, InboundChatService inboundChatService,
			OutboundChatService outboundChatService) {
		Map<String, WebSocketHandler> urlMap = new HashMap<>();
		urlMap.put("/topic/comments.new", commentService);
		 urlMap.put("/app/chatMessage.new", inboundChatService);
		 urlMap.put("/topic/chatMessage.new", outboundChatService);

		SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
		mapping.setOrder(10);
		mapping.setUrlMap(urlMap);

		return mapping;
	}

	/**
	 * This spring bean is critical to the infrastructure of WebSocket messaging. It
	 * connects Spring's DispatcherHandler to a WebSocketHandler, allowing URIs to
	 * be mapped onto handler methods
	 */
	@Bean
	WebSocketHandlerAdapter handlerAdapter() {
		return new WebSocketHandlerAdapter();
	}
}
