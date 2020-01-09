package com.ziemniak.webserv.controllers;

import com.google.common.io.ByteStreams;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@Api(description = "Herbatka dla każdego")
public class Teapot {
	@GetMapping("/coffee")
	@ApiOperation(value = "Wysyła żądanie o kawe", produces = "text")
	@ApiResponse(code = 418, message = "Tutaj serwujemy jedynie herbatke")
	public ResponseEntity<String> serveCoffee() {
		return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body("Nu");
	}

	@GetMapping(value = "/tea", produces = MediaType.IMAGE_PNG_VALUE)
	@ApiOperation(value = "Wysyła żądanie o herbatkę",produces = "image/png")
	@ApiResponse(message = "Herbatka jest gotowa do spożycia", code = 200)
	public byte[] serveTea() throws IOException {
		File f = ResourceUtils.getFile("classpath:tea.png");
		return Files.readAllBytes(f.toPath());
	}
}
