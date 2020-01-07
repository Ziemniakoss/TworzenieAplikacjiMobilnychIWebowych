package com.ziemniak.webserv.controllers;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//todo
public class BibliographyFilesREST {

	@GetMapping("/bibliography/files")
	public void getAllFilesInBibliography() {

	}

	@PostMapping("/bibliography/files/add")
	public void addFileToBibliography() {

	}

	@DeleteMapping("/bibliography/files/delete")
	public void deleteFileFromBibliography(){

	}
}
