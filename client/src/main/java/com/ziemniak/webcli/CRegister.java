package com.ziemniak.webcli;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller allowing to register in system.
 */
@Controller
@RequestMapping("/register")
public class CRegister {

	@GetMapping
	public String get() {
		return "register";
	}

	/**
	 * Tries to register user in system. User can only be registered
	 * if he/she/it/* has unique username and if password was correctly typed both as
	 * password and as validation password
	 *
	 * @param username         unique username
	 * @param password         password
	 * @param validatePassword password typed second time
	 * @param model            model for view
	 * @return if input was correct and system managed to register new user it returns view name "home",
	 * otherwise it returns "register" and sets errors in model according to what went wrong
	 */
	@PostMapping
	public String registerUser(@RequestParam String username, @RequestParam String password, @RequestParam String validatePassword, Model model) {

		String url = ClientApplication.URL_TO_SERVER + "/auth/register?username="+username+"&password="+password+"&validatePassword="+validatePassword;
		RestTemplate rt = new RestTemplate();
		try {
			System.out.println();
			ResponseEntity<RegisterResponse> response = rt.postForEntity(
					url,null, RegisterResponse.class);
			System.out.println(response.getStatusCode());
			System.out.println(response.getBody());
			model.addAttribute("username",username);
			return "home";
		} catch (RestClientException rce) {
			rce.printStackTrace();
			System.err.println(rce.getMessage());
			System.out.println(rce);
			model.addAttribute("error", "Please correct your data");
			return "register";
		}
	}
}
