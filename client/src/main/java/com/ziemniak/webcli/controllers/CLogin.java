package com.ziemniak.webcli.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ziemniak.webcli.ClientApplication;
import com.ziemniak.webcli.dto.LoginNegativeResponseDto;
import com.ziemniak.webcli.dto.LoginPositiveResponseDto;
import com.ziemniak.webcli.dto.LoginRequestDTO;
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
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * Controller allowing to lo into system
 */
@Controller
@RequestMapping("/login")
public class CLogin {
	private final Logger log = LoggerFactory.getLogger(CLogin.class);

	@GetMapping
	public String get(Model model, HttpServletResponse response) {
		response.addCookie(new Cookie("jwt", ""));
		System.out.println(model.getAttribute("user"));
		model.addAttribute("user", new LoginRequestDTO());
		return "login";
	}

	@PostMapping
	public String processLoginRequest(@Valid @ModelAttribute LoginRequestDTO loginReq, Errors errors, Model model, HttpServletResponse httpServletResponse) {
		if (errors.hasErrors()) {
			processErrors(errors);
			return "login";
		}
		try {
			String jwt = getJwt(loginReq);
			Cookie cookie = new Cookie("jwt", jwt);
			cookie.setHttpOnly(true);
			cookie.setMaxAge(5 * 60);
			httpServletResponse.addCookie(cookie);
			return "redirect:/home";
		} catch (HttpClientErrorException e) {
			if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {//Złe dane logowania
				LoginNegativeResponseDto resp;
				try {
					ObjectMapper mapper = new ObjectMapper();
					resp = mapper.readValue(e.getResponseBodyAsString(), LoginNegativeResponseDto.class);
					String sb = "Failed to log in with username \"" +
							loginReq.getUsername() +
							"\"(http 401):" +
							resp.getReason();
					model.addAttribute("errorreason", resp.getReason());
					model.addAttribute("username", loginReq.getUsername());
				} catch (JsonProcessingException ex) {
					log.error("Error occurred while parsing negative response from server(" +
							e.getStatusCode() + ")to login: " + ex.getMessage());
				}
			}
			return "login";
		} catch (HttpServerErrorException e) {
			String message = "Server experienced error(" +
					e.getStatusText() +
					"):" +
					e.getMessage();
			log.error(message);
			model.addAttribute("errorreason", "Server experienced error, please try again");
			return "login";
		} catch (Exception e) {
			String message = "Unknown error occurred while logging to server" +
					e.getClass() +
					": " +
					e.getMessage();
			log.error(message);
			model.addAttribute("errorreason", "Unknown error occurred, please try again later");
			return "login";
		}
	}

	/**
	 * Pobiera z servera JWT dla podanych danych logowania
	 * @param loginReq dane logowania
	 * @return pobrany JWT
	 * @throws HttpClientErrorException gdy dane logowania są niepoprawne
	 * @throws HttpServerErrorException gdy server naotka problem przy przetwarzaniu żadania
	 */
	private String getJwt(LoginRequestDTO loginReq) throws HttpClientErrorException, HttpServerErrorException {
		log.info("Trying to get jwt for user \"" + loginReq.getUsername() + "\"");
		String url = ClientApplication.URL_TO_SERVER + "/auth/login";
		RestTemplate rt = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<LoginRequestDTO> e = new HttpEntity<>(loginReq, headers);
		LoginPositiveResponseDto resp = rt.postForObject(url, e, LoginPositiveResponseDto.class);
		String jwt = resp.getJwt();
		log.info("Recived jwt for user \"" + loginReq.getUsername() + "\"");
		return jwt;
	}

	private void processErrors(Errors errors) {
		StringBuilder sb = new StringBuilder();
		sb.append("Login request had ");
		sb.append(errors.getErrorCount());
		sb.append(" errors: ");
		boolean first = true;
		for (ObjectError e : errors.getAllErrors()) {
			if (first) {
				sb.append(e.toString());
				first = false;
			} else {
				sb.append(", ");
				sb.append(e.toString());
			}
		}
		log.warn(sb.toString());
	}
}
