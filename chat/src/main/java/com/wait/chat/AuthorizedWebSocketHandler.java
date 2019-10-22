package com.wait.chat;

import java.security.Principal;

import org.springframework.security.core.Authentication;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;

import reactor.core.publisher.Mono;

/**
 * @author collinewaitire 22 Oct 2019
 */
abstract class AuthorizedWebSocketHandler implements WebSocketHandler {

	@Override
	public final Mono<Void> handle(WebSocketSession session) {
		return session.getHandshakeInfo().getPrincipal().filter(this::isAuthorized).then(doHandle(session));
	}

	private boolean isAuthorized(Principal principal) {
		Authentication authentication = (Authentication) principal;
		return authentication.isAuthenticated() && authentication.getAuthorities().contains("ROLE_USER");
	}

	abstract protected Mono<Void> doHandle(WebSocketSession session);
}
