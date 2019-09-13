package com.wait.mypic;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

/**
 * @author collinewaitire 13 Sep 2019
 */

/*
 * We are pre-loading out MongoDB data store. For such operations, it's
 * recommended to actually use the blocking API. That's because when launching
 * an application, there is a certain risk of a thread lock issue when both the
 * web container as well as our hand-written loader are starting up. Since
 * Spring Boot also creates a MongoOperations object, we can simply grab hold of
 * that.
 */
@Component
public class InitDatabase {

	@Bean
	CommandLineRunner init(MongoOperations operations) {
		return args -> {
			operations.dropCollection(Image.class);

			operations.insert(new Image("1", "my-first-image.jpg"));
			operations.insert(new Image("2", "my-second-image.jpg"));
			operations.insert(new Image("3", "my-third-image.jpg"));

			operations.findAll(Image.class).forEach(image -> {
				System.out.println(image.toString());
			});
		};
	}
}
