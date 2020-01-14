package com.ziemniak.webcli.controllers;

import com.ziemniak.webcli.ClientApplication;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Kontroler odpowiedzialny za logowanie do systemu
 */
@Controller
@RequestMapping("/login")
public class CLogin {
	private final Logger log = LoggerFactory.getLogger(CLogin.class);

	@GetMapping
	public String get(Model model, HttpServletResponse response) {
		response.addCookie(new Cookie("jwt", ""));
		model.addAttribute("user", new LoginRequestDTO());
		return "login";
	}

	@PostMapping
	public String processLoginRequest(@Valid @ModelAttribute LoginRequestDTO loginReq, Errors errors, Model model, HttpServletResponse httpServletResponse) {
		if (errors.hasErrors()) {
			List<String> e = new ArrayList<>();
			for (ObjectError ee : errors.getAllErrors()) {
				System.out.println(ee.getDefaultMessage());
				e.add(ee.getDefaultMessage());
			}
			model.addAttribute("errorsList", e);
			return "login";
		}
		try {
			String jwt = getJwt(loginReq);
			Cookie cookie = new Cookie("jwt", jwt);
			cookie.setHttpOnly(true);
			cookie.setMaxAge(5 * 60);
			httpServletResponse.addCookie(cookie);
			return "redirect:/myfiles";
		} catch (HttpClientErrorException e) {
			if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {//Złe dane logowania
				List<String> ee = new ArrayList<>();
				ee.add("Złe dane logowania");
				model.addAttribute("errorsList", ee);
			}
			return "login";
		} catch (HttpServerErrorException e) {
			String message = "Server experienced error(" +
					e.getStatusText() +
					"):" +
					e.getMessage();
			log.error(message);
			return "login";
		} catch (Exception e) {
			log.error("Unknown error occurred while logging to server" + e.getClass() + ": " + e.getMessage());
			return "login";
		}
	}

	/**
	 * Pobiera z servera JWT dla podanych danych logowania
	 *
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
}
