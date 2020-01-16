package com.ziemniak.webcli.controllers;

import com.ziemniak.webcli.ClientApplication;
import com.ziemniak.webcli.File;
import com.ziemniak.webcli.dto.FileUploadDTO;
import com.ziemniak.webcli.dto.FileUploadPositiveResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;

@Controller
public class MyFilesController {
	private final Logger log = LoggerFactory.getLogger(MyFilesController.class);

	@GetMapping("/myfiles")
	public String myFiles(@CookieValue(value = "jwt", defaultValue = "") String jwt, Model model) {
		if ("".equals(jwt)) {
			return "redirect:/login";
		}
		File[] files = fetchFileList(jwt);
		model.addAttribute("fileList", files);
		return "myfiles";
	}

	private File[] fetchFileList(String jwt) {
		RestTemplate rt = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + jwt);
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity entity = new HttpEntity<>("", headers);
		String url = ClientApplication.URL_TO_SERVER + "/files/getall";
		ResponseEntity<File[]> response = rt.exchange(url, HttpMethod.GET, entity, com.ziemniak.webcli.File[].class, new HashMap<>());
		return response.getBody();
	}

	@PostMapping("/myfiles/upload")
	public String uploadFile(@CookieValue(value = "jwt", defaultValue = "") String jwt,
							 @RequestParam("file") MultipartFile file, Model model) {
		if ("".equals(jwt)) {
			return "redirect:/login";
		}
		if (file.isEmpty()) {
			log.warn("Someone tried to post emty file");
			return "redirect:/myfiles";
		}
		try {
			RestTemplate rt = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
			HttpEntity<FileUploadDTO> entity =
					new HttpEntity<>(new FileUploadDTO(file.getOriginalFilename(), file.getBytes()), headers);
			String url = ClientApplication.URL_TO_SERVER +"/files/add";
			rt.exchange(url,HttpMethod.POST, entity, String.class);
			log.info("Uploded file "+file.getName()+ " tp server");
		} catch (IOException e) {
			log.error("Error occured while sending file from user to server: " + e.getMessage());
			e.printStackTrace();
		}
		return "myfiles";
	}
}