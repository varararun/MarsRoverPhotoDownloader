package com.avarghese.marsrover.controller;

import com.avarghese.marsrover.client.MarsRoverAPIClient;
import com.avarghese.marsrover.domain.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class HomeController {

	@Autowired
	MarsRoverAPIClient client;

	@GetMapping("/")
	public String queryForm(org.springframework.ui.Model model) {
		model.addAttribute("model", new Model());
		return "home";
	}

	@PostMapping("/")
	public String querySubmit(@ModelAttribute Model model) {
		List<String> results = client.getPhotos(model.getCamera(), model.getDate(), model.isDryRun());
		model.setResults(results);
		return "result";
	}

}
