package com.wait.chat;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author collinewaitire 21 Oct 2019
 */
@Controller
public class HomeController {

	@GetMapping("/")
	public String index(@AuthenticationPrincipal Authentication auth, Model model) {
		model.addAttribute("authentication", auth);
		return "index";
	}

}
