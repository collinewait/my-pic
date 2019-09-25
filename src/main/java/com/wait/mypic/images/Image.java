package com.wait.mypic.images;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

/**
 * @author collinewaitire 8 Sep 2019
 */
@Data
@Document
public class Image {

	@Id
	private final String id;
	private final String name;

	public Image(String id, String name) {
		this.id = id;
		this.name = name;
	}
}
