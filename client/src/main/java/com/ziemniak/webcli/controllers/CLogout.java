package com.ziemniak.webcli.controllers;

import com.ziemniak.webcli.ClientApplication;
import com.ziemniak.webcli.dto.BlacklistJwtRequestDTO;
import com.ziemniak.webcli.dto.BlacklistJwtResponseDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
public class CLogout {
	@PostMapping(value = "/logout")
	public String logout(@CookieValue(value = "jwt")String jwt, HttpServletResponse httpServletResponse){
		BlacklistJwtRequestDTO req = new BlacklistJwtRequestDTO(jwt);
		RestTemplate rt = new RestTemplate();
		String url = ClientApplication.URL_TO_SERVER+"/auth/logout";
		rt.postForObject(url,req, BlacklistJwtResponseDTO.class);
		httpServletResponse.addCookie(new Cookie("jwt",""));

		return "redirect:/";
	}
}
