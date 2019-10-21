package com.wait.chat;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author collinewaitire 21 Oct 2019
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

	@Id
	private String id;
	private String username;
	private String password;
	private String[] roles;
}
