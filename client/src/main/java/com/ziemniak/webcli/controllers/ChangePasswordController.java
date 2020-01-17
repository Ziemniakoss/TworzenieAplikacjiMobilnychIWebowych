package com.ziemniak.webcli.controllers;

import com.ziemniak.webcli.ClientApplication;
import com.ziemniak.webcli.models.MChangePassword;
import org.json.JSONArray;
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


@Controller
public class ChangePasswordController {
	@GetMapping("/changepassword")
	public String get(Model model, @CookieValue(value = "jwt", defaultValue = "") String jwt) {
		if (jwt.equals("")) {
			return "redirect:/login";
		}
		model.addAttribute("req", new MChangePassword());
		return "changePassword";
	}

	@PostMapping("/changepassword")
	public String processChangePasswordRequest(@CookieValue(value = "jwt", defaultValue = "") String jwt,
											   @ModelAttribute MChangePassword req, Model model){
		if(jwt.equals("")){
			return "redirect:/login";
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION,"Bearer "+jwt);
		HttpEntity entity = new HttpEntity(req, headers);
		RestTemplate rt = new RestTemplate();
		String url = ClientApplication.URL_TO_SERVER +"/users/changepassword";
		try{
			rt.exchange(url, HttpMethod.POST,entity,String.class);
			return "redirect:/passwordChanged";
		}catch (HttpClientErrorException e){
			JSONArray array = new JSONArray(e.getResponseBodyAsString());
			model.addAttribute("errors",array.toList());
			return "changePassword";
		}
	}

	@GetMapping("/passwordChanged")
	public String get(){
		return "passwordChanged";
	}
}
