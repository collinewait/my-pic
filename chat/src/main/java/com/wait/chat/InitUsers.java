package com.wait.chat;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;

/**
 * @author collinewaitire 21 Oct 2019
 */
@Configuration
public class InitUsers {

	@Bean
	CommandLineRunner initializeUsers(MongoOperations operations) {
		return args -> {
			operations.dropCollection(User.class);

			operations.insert(new User(null, "colline", "wait", new String[] { "ROLE_USER", "ROLE_ADMIN" }));
			operations.insert(new User(null, "paul", "Ona", new String[] { "ROLE_USER" }));

			operations.findAll(User.class).forEach(user -> {
				System.out.println("Loaded " + user);
			});
		};
	}
}
