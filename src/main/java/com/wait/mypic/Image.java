package com.wait.mypic;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author collinewaitire 8 Sep 2019
 */
@Data
@NoArgsConstructor
public class Image {

	private String id;
	private String name;

	public Image(String id, String name) {
		this.id = id;
		this.name = name;
	}
}
