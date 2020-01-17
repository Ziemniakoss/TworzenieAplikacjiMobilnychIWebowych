package com.ziemniak.webcli.controllers;

import com.ziemniak.webcli.ClientApplication;
import com.ziemniak.webcli.dto.FileSharedForUserDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;


@Controller
public class SharedFilesForMeController {
	@GetMapping("/shared")
	public String get(@CookieValue(value = "jwt", defaultValue = "") String jwt, Model model) {
		if ("".equals(jwt)) {
			return "redirect:/login";
		}
		RestTemplate rt = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION,"Bearer "+jwt);
		HttpEntity entity = new HttpEntity(headers);
		String url = ClientApplication.URL_TO_SERVER+"/files/shared/forme";
		FileSharedForUserDTO[] files = rt.exchange(url, HttpMethod.GET, entity, FileSharedForUserDTO[].class).getBody();
		System.out.println(files.length);
		model.addAttribute("files",files);
		return "sharedForMe";

	}
}
