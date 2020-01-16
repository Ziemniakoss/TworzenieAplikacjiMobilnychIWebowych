package com.ziemniak.webcli.controllers;

import com.ziemniak.webcli.ClientApplication;
import com.ziemniak.webcli.dto.FileSharedByUserDTO;
import com.ziemniak.webcli.dto.RevokeAccessToFileDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
public class MySharedFilesController {

	@GetMapping("/myfiles/shared")
	public String mySharedFiles(@CookieValue(value = "jwt", defaultValue = "") String jwt, Model model) {
		if ("".equals(jwt)) {
			return "redirect:/login";
		}
		model.addAttribute("files", getAllShared(jwt));
		model.addAttribute("request", new RevokeAccessToFileDTO());
		return "mysharedfiles";
	}

	private FileSharedByUserDTO[] getAllShared(String jwt) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + jwt);
		HttpEntity entity = new HttpEntity(headers);
		String url = ClientApplication.URL_TO_SERVER + "/files/shared/mine";
		RestTemplate rt = new RestTemplate();
		return rt.exchange(url, HttpMethod.GET, entity, FileSharedByUserDTO[].class).getBody();
	}

	@PostMapping("/myfiles/shared/revokeaccess")
	public String revokeAccess(@CookieValue(value = "jwt", defaultValue = "") String jwt, @ModelAttribute RevokeAccessToFileDTO req) {
		if ("".equals(jwt)) {
			return "redirect:/login";
		}
		System.out.println(req);
		RestTemplate rt = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + jwt);
		HttpEntity e = new HttpEntity(req,headers);
		String url = ClientApplication.URL_TO_SERVER +"/files/revokeaccess";
		try {
			rt.exchange(url,HttpMethod.POST,e,String.class);
		}catch (HttpClientErrorException ee){
			ee.printStackTrace();
		}
		return "redirect:/myfiles/shared";
	}
}
