package com.ziemniak.webserv.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Index {
	@GetMapping("/")
	public String redirectToDocumentation(){
		return "redirect:/swagger-ui.html";
	}
}
