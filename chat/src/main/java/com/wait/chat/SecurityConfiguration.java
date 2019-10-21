package com.wait.chat;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;

/**
 * @author collinewaitire 21 Oct 2019
 */
@EnableWebFluxSecurity
public class SecurityConfiguration {

	@Bean
	SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) {
		return http.authorizeExchange().pathMatchers("/**").authenticated().and().httpBasic()
				.securityContextRepository(new WebSessionServerSecurityContextRepository()).and().csrf().disable()
				.build();
	}
}
