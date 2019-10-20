package com.ziemniak.webcli;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

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
		HttpURLConnection http = null;
		try {
			URL url = new URL("http://" + ClientApplication.URL_TO_SERVER + "/auth/register");
			URLConnection con = url.openConnection();
			http = (HttpURLConnection) con;
			http.setRequestMethod("POST");
			http.setDoOutput(true);

			Map<String, String> params = new HashMap<>();
			params.put("username", username);
			params.put("password", password);
			params.put("validatePassword", validatePassword);

			StringJoiner sj = new StringJoiner("&");
			for (Map.Entry<String, String> entry : params.entrySet()) {
				sj.add(URLEncoder.encode(entry.getKey(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue(), "UTF-8"));
			}
			byte[] out = sj.toString().getBytes(StandardCharsets.UTF_8);
			int lenght = out.length;

			http.setFixedLengthStreamingMode(lenght);
			//http.setRequestProperty();
			try (OutputStream os = http.getOutputStream()) {
				os.write(out);
				os.flush();
			}

			InputStream inputStream = http.getInputStream();
			int c;
			while ((c = inputStream.read()) != -1) {
				System.out.println((char) c);
			}
			inputStream.close();
			System.out.println(http.getResponseCode());
			System.out.println(http.getResponseMessage());
			model.addAttribute("username", username);
			return "home";
		} catch (MalformedURLException e) {
			model.addAttribute("error", "Malformed URL, contact dev");
			return "register";
		} catch (IOException e) {
			model.addAttribute("error", "Please correct your data");
			return "register";
		}
	}
}
