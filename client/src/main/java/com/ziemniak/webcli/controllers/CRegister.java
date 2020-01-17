package com.ziemniak.webcli.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ziemniak.webcli.ClientApplication;
import com.ziemniak.webcli.dto.RegisterRequestDTO;
import com.ziemniak.webcli.dto.RegisterResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller allowing to register in system.
 */
@Controller
@RequestMapping("/register")
public class CRegister {
	private final Logger log = LoggerFactory.getLogger(CRegister.class);

	@GetMapping
	public String get(Model model) {
		model.addAttribute("user", new RegisterRequestDTO());
		return "register";
	}

	@PostMapping
	public String registerUser(@Valid @ModelAttribute("user") RegisterRequestDTO registerRequest, Errors errors, Model model) {
		if (errors.hasErrors()) {
			log.warn("Register request had " + errors.getErrorCount() + " errors");
			model.addAttribute("errorList", errors.getAllErrors().stream()
					.map(ObjectError::getDefaultMessage)
					.sorted((s1, s2) -> {
						if(s1.length() > s2.length()){
							return 1;
						}else if(s1.length() == s2.length()){
							return 0;
						}else {
							return -1;
						}
					})
					.collect(Collectors.toList()));
			return "register";
		}
		RegisterResponseDTO response;
		try {
			String url = ClientApplication.URL_TO_SERVER + "/auth/register";
			RestTemplate rt = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<RegisterRequestDTO> e = new HttpEntity<>(registerRequest, headers);

			response = rt.postForObject(url, e, RegisterResponseDTO.class);
			if (response.isAccepted()) {//todo można zalogować i przekierwać na stronę główną
				return "redirect:/";
			}
			model.addAttribute("errorList", response.getErrors());
		} catch (HttpClientErrorException e) {
			if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
				ObjectMapper mapper = new ObjectMapper();
				try {
					response = mapper.readValue(e.getResponseBodyAsString(), RegisterResponseDTO.class);
					System.out.println(String.join(" ", response.getErrors()));
					model.addAttribute("errorList", response.getErrors());
				} catch (JsonProcessingException ex) {
					log.error("Error while parsing negative respones to register request: " + ex.getMessage());
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return "register";
	}
}
