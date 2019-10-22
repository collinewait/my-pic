package com.wait.mypic.images;

import java.util.Collections;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.wait.mypic.ImagesConfiguration;

/**
 * @author collinewaitire 17 Oct 2019
 */
@Component
public class CommentHelper {

	private final RestTemplate restTemplate;
	private final ImagesConfiguration imagesConfiguration;

	public CommentHelper(RestTemplate restTemplate, ImagesConfiguration imagesConfiguration) {
		this.restTemplate = restTemplate;
		this.imagesConfiguration = imagesConfiguration;
	}

	@HystrixCommand(fallbackMethod = "defaultComments")
	public List<Comment> getComments(Image image, String sessionId) {
		ResponseEntity<List<Comment>> results = restTemplate.exchange("http://COMMENTS/comments/{imageId}",
				HttpMethod.GET, new HttpEntity<>(new HttpHeaders() {
					private static final long serialVersionUID = 1L;

					{
						String credentials = imagesConfiguration.getCommentsUser() + ":"
								+ imagesConfiguration.getCommentsPassword();
						String token = new String(Base64Utils.encode(credentials.getBytes()));
						set(AUTHORIZATION, "Basic " + token);
						set("Cookie", "SESSION=" + sessionId);
					}
				}), new ParameterizedTypeReference<List<Comment>>() {
				}, image.getId());
		return results.getBody();
	}

	public List<Comment> defaultComments(Image image, String sessionId) {
		return Collections.emptyList();
	}

}
