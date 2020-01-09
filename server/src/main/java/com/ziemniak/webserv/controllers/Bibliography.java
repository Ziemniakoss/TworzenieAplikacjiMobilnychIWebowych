package com.ziemniak.webserv.controllers;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//todo
public class Bibliography {

	@GetMapping("/bibliography/get")
	public void getBibliographyInfo() {

	}

	@GetMapping("/bibliography/getall")
	public void getAllBibliographies() {

	}

	@PostMapping("/bibliography/add")
	public void createBibliography() {

	}

}
