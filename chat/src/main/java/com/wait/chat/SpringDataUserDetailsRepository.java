package com.wait.chat;

import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

/**
 * @author collinewaitire 21 Oct 2019
 */
@Component
public class SpringDataUserDetailsRepository implements ReactiveUserDetailsService {

	private final UserRepository repository;

	public SpringDataUserDetailsRepository(UserRepository repository) {
		this.repository = repository;
	}

	@Override
	public Mono<UserDetails> findByUsername(String username) {
		return repository.findByUsername(username).map(user -> User.withDefaultPasswordEncoder()
				.username(user.getUsername()).password(user.getPassword()).authorities(user.getRoles()).build());
	}
}