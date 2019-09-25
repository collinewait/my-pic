package com.wait.mypic;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.wait.mypic.images.Image;

/**
 * @author collinewaitire 14 Sep 2019
 */
public class ImageTests {
	@Test
	public void imagesManagedByLombokShouldWork() {
		Image image = new Image("id", "file-name.jpg");
		assertThat(image.getId()).isEqualTo("id");
		assertThat(image.getName()).isEqualTo("file-name.jpg");
	}
}
