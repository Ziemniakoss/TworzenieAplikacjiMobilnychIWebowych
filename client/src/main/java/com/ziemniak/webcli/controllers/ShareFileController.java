package com.ziemniak.webcli.controllers;

import com.ziemniak.webcli.ClientApplication;
import com.ziemniak.webcli.dto.ShareFileRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Controller
public class ShareFileController {
	private final Logger log = LoggerFactory.getLogger(ShareFileController.class);

	@GetMapping("/sharefile/{id}")
	public String get(Model model, @CookieValue(value = "jwt", defaultValue = "") String jwt,
					  @PathVariable int id) {
		if ("".equals(jwt)) {
			return "redirect:/login";
		}
		ShareFileRequestDTO req = new ShareFileRequestDTO();
		model.addAttribute("req",req);
		return "sharefile";
	}

	@PostMapping("/sharefile")
	public String shareFile(@ModelAttribute ShareFileRequestDTO request, @CookieValue(value = "jwt", defaultValue = "")String jwt){
		if("".equals(jwt)){
			return "redirect:/login";
		}
		System.out.println(request);
		RestTemplate rt = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization","Bearer "+jwt);
		HttpEntity entity = new HttpEntity(request,headers);
		String url = ClientApplication.URL_TO_SERVER+"/files/share";
		rt.exchange(url, HttpMethod.POST,entity,String.class);
		log.info("User shared file "+request.getFileId()+" to user "+ request.getUsername());

		return "redirect:/myfiles/shared";
	}


}
