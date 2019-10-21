package com.wait.mypic.images;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author collinewaitire 8 Sep 2019
 */
@Data
@AllArgsConstructor
public class Image {

	@Id
	private String id;
	private String name;
	private String owner;
}
