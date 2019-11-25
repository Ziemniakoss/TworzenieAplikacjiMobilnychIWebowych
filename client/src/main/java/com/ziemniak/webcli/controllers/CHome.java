package com.ziemniak.webcli.controllers;

import com.ziemniak.webcli.ClientApplication;
import com.ziemniak.webcli.dto.FileUploadPositiveResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/home")
public class CHome {
	private final Logger log = LoggerFactory.getLogger(CHome.class);

	@GetMapping()
	public String home(@CookieValue(value = "jwt", defaultValue = "") String jwt) {
		if ("".equals(jwt)) {
			return "redirect:/login";
		}
		//pobranie listy publikacji
		//todo
		return "home";
	}

	@PostMapping()
	public String postFile(@CookieValue(value = "jwt", defaultValue = "") String jwt, @RequestParam("file") MultipartFile file, Model model) throws IOException {
		if ("".equals(jwt)) {
			return "redirect:/login";
		}
		if (file.isEmpty()) {
			log.warn("Tried to upload empty file");
			model.addAttribute("errorreason", "File was empty");
			return "redirect:/home";
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.set("Cookie", "jwt=" + jwt);

		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("file", new InputStreamResource(file.getInputStream()));
		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(body, headers);
		String url = ClientApplication.URL_TO_SERVER + "/files/add";
		RestTemplate restTemplate = new RestTemplate();
		try {
			restTemplate.postForEntity(url, httpEntity, FileUploadPositiveResponseDTO.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "redirect:/home";
	}
}
