package com.wait.mypic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author collinewaitire 14 Sep 2019
 */
@RunWith(SpringRunner.class)
@WebFluxTest(controllers = HomeController.class)
@Import({ ThymeleafAutoConfiguration.class })
public class HomeControllerTests {
	@Autowired
	WebTestClient webClient;

	@MockBean
	ImageService imageService;

	@Test
	public void baseRouteShouldListAllImages() {
		// given
		Image image1 = new Image("1", "image1.png");
		Image image2 = new Image("2", "image2.png");

		given(imageService.findAllImages()).willReturn(Flux.just(image1, image2));

		// when
		EntityExchangeResult<String> result = webClient.get().uri("/").exchange()
				.expectStatus().isOk().expectBody(String.class).returnResult();

		// then
		verify(imageService).findAllImages();
		verifyNoMoreInteractions(imageService);
		assertThat(result.getResponseBody()).contains("<title>My Pic</title>")
				.contains("<a href=\"/images/image1.png/raw\">")
				.contains("<a href=\"/images/image2.png/raw\">");
	}

	@Test
	public void fetchingImageShouldWork() {
		given(imageService.findOneImage(any()))
				.willReturn(Mono.just(new ByteArrayResource("data".getBytes())));

		webClient.get().uri("/images/image1.png/raw").exchange().expectStatus()
				.isOk().expectBody(String.class).isEqualTo("data");
		verify(imageService).findOneImage("image1.png");
		verifyNoMoreInteractions(imageService);
	}

	@Test
	public void fetchingNullImageShouldFail() throws IOException {
		Resource resource = mock(Resource.class);
		given(resource.getInputStream())
				.willThrow(new IOException("Bad file"));
		given(imageService.findOneImage(any())).willReturn(Mono.just(resource));

		webClient.get().uri("/images/image1.png/raw").exchange().expectStatus()
				.isBadRequest().expectBody(String.class)
				.isEqualTo("Could't find image1.png => Bad file");

		verify(imageService).findOneImage("image1.png");
		verifyNoMoreInteractions(imageService);
	}

	@Test
	public void deleteImageShouldWork() {
		given(imageService.deleteImage(any())).willReturn(Mono.empty());

		webClient.delete().uri("/images/image1.png").exchange().expectStatus()
				.isSeeOther().expectHeader().valueEquals(HttpHeaders.LOCATION, "/");

		verify(imageService).deleteImage("image1.png");
		verifyNoMoreInteractions(imageService);

	}
}
