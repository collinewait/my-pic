package com.wait.mypic;

import java.io.IOException;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author collinewaitire 8 Sep 2019
 */
@Controller
public class HomeController {
	private static final String BASE_PATH = "/images";
	private static final String FILENAME = "{filename:.+}";

	private final ImageService imageService;

	public HomeController(ImageService imageService) {
		this.imageService = imageService;
	}

	@GetMapping(value = BASE_PATH + "/" + FILENAME
			+ "/raw", produces = MediaType.IMAGE_JPEG_VALUE)
	@ResponseBody // @ResponseBody indicates that this method's response will be
								// written directly into the HTTP response body
	public Mono<ResponseEntity<?>> oneRawImage(@PathVariable String filename) {
		return imageService.findOneImage(filename).map(resource -> {
			try {
				return ResponseEntity.ok().contentLength(resource.contentLength())
						.body(new InputStreamResource(resource.getInputStream()));
			} catch (IOException e) {
				return ResponseEntity.badRequest()
						.body("Could't find " + filename + " => " + e.getMessage());
			}
		});
	}

	@PostMapping(value = BASE_PATH)
	public Mono<String> createFile(
			@RequestPart(name = "file") Flux<FilePart> files) {
		/*
		 * It is important to remember that we aren't issuing .then() against the
		 * flux of files. Instead, the image service hands us back a Mono<Void> that
		 * signals when it has completed processing all files. It is that Mono which
		 * we are chaining an additional call to return back the redirect.
		 */
		return imageService.createImage(files).then(Mono.just("redirect:/"));
	}

	@DeleteMapping(BASE_PATH + "/" + FILENAME)
	public Mono<String> deleteFile(@PathVariable String filename) {
		return imageService.deleteImage(filename).then(Mono.just("redirect:/"));
	}

	public Mono<String> index(Model model) {
		/*
		 * It is important to understand that we don't assign a list of images to
		 * the template model's attribute. we assign a lazy Flux of images, which
		 * means that the model won't be populated with real data until Reactive
		 * Spring subscribes for the data. Only then will the code actually start
		 * fetching image data.
		 */
		model.addAttribute("images", imageService.findAllImages());
		return Mono.just("index");
	}

}
