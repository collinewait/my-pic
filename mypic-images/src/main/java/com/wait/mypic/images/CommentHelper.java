package com.wait.mypic.images;

import java.util.Collections;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

/**
 * @author collinewaitire 17 Oct 2019
 */
@Component
public class CommentHelper {

	private final RestTemplate restTemplate;

	public CommentHelper(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@HystrixCommand(fallbackMethod = "defaultComments")
	public List<Comment> getComments(Image image) {
		return restTemplate.exchange("http://COMMENTS/comments/{imageId}",
				HttpMethod.GET, null, new ParameterizedTypeReference<List<Comment>>() {
				}, image.getId()).getBody();
	}

	public List<Comment> defaultComments(Image image) {
		return Collections.emptyList();
	}

}
