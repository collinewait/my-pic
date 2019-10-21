package com.wait.chat;

import org.springframework.data.repository.Repository;

import reactor.core.publisher.Mono;

/**
 * @author collinewaitire 21 Oct 2019
 */
public interface UserRepository extends Repository<User, String> {

	Mono<User> findByUsername(String username);
}