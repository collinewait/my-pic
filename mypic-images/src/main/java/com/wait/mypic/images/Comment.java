package com.wait.mypic.images;

import org.springframework.data.annotation.Id;

import lombok.Data;

/**
 * @author collinewaitire 25 Sep 2019
 */

@Data
public class Comment {

	@Id
	private String id;
	private String imageId;
	private String comment;
}
