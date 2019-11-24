package com.ziemniak.webcli.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CHome {
	@GetMapping("/home")
	public String home(){
		return "home";
	}
}
