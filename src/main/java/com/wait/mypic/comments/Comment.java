package com.wait.mypic.comments;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

/**
 * @author collinewaitire 25 Sep 2019
 */
@Data
@Document
public class Comment {

	@Id
	private String id;
	private String imageId;
	private String comment;
}
