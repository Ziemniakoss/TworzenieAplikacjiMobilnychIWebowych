package com.ziemniak.webcli.controllers;

import com.ziemniak.webcli.File;
import com.ziemniak.webcli.dto.ShareFileRequestDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class ShareFileController {

	@GetMapping("/sharefile/{id}")
	public String get(Model model, @CookieValue(value = "jwt", defaultValue = "") String jwt,
					  @PathVariable int id) {
		if ("".equals(jwt)) {
			return "redirect:/login";
		}
		ShareFileRequestDTO req = new ShareFileRequestDTO();
		req.setId(id);
		model.addAttribute("req",req);
		return "sharefile";
	}

	@PostMapping("/sharefile")
	public String shareFile(@ModelAttribute ShareFileRequestDTO request, @CookieValue(value = "jwt", defaultValue = "")String jwt){
		if("".equals(jwt)){
			return "redirect:/login";
		}

		//todo wysłać teraz na strone
	}


}
