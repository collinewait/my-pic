package com.wait.mypic;

import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.WebSession;

import com.wait.mypic.images.CommentHelper;
import com.wait.mypic.images.ImageService;

import reactor.core.publisher.Mono;

/**
 * @author collinewaitire 8 Sep 2019
 */
@Controller
public class HomeController {

	private final ImageService imageService;

	private final CommentHelper commentHelper;

	public HomeController(ImageService imageService, CommentHelper commentHelper) {
		this.imageService = imageService;
		this.commentHelper = commentHelper;
	}

	@GetMapping("/")
	public Mono<String> index(Model model, WebSession webSession) {
		model.addAttribute("images", imageService.findAllImages().map(image -> new HashMap<String, Object>() {
			private static final long serialVersionUID = 1L;

			{
				put("id", image.getId());
				put("name", image.getName());
				put("owner", image.getOwner());
				put("comments", commentHelper.getComments(image, webSession.getId()));
			}
		}));

		return Mono.just("index");
	}

}
