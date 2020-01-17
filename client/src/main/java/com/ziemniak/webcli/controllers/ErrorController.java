package com.ziemniak.webcli.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {
	@GetMapping("/error/401")
	public String error401(){
		return "error401";
	}
	@GetMapping("/error/404")
	public String error404(){
		return "error404";
	}
	@GetMapping("/error/server")
	public String serverError(){
		return "errorServer";
	}
	@GetMapping("/error/client")
	public String clientError(){
		return "errorClientUnknown";
	}
}
