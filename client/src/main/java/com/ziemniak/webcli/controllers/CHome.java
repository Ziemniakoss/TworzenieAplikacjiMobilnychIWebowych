package com.ziemniak.webcli.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class CHome {
	@GetMapping()
	public String home(@CookieValue(value = "jwt",defaultValue = "") String jwt){
		if("".equals(jwt)){
			return "redirect:/login";
		}
		//pobranie listy publikacji
		//todo
		return "home";
	}
}
