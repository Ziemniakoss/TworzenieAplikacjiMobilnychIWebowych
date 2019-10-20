package com.ziemniak.webcli;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller

public class CIndex {

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(){
		return "index";
	}
}
