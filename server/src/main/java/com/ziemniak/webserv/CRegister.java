package com.ziemniak.webserv;

import com.ziemniak.webserv.redis.RedisAccess;
import com.ziemniak.webserv.redis.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Controller responsible only for registering new users
 */
@RestController
@CrossOrigin(origins = "*")
public class CRegister {

	/**
	 * Registers new user only if there is no user with given
	 * username and if passwords match each other
	 *
	 * @param username         username of new user
	 * @param password         password for user
	 * @param validatePassword same password typed second time for validation
	 * @return ResponseEntity with code:
	 * <ul>
	 *     <li>200 if system registered new user</li>
	 *     <li>400 if there is already user with this username or passwords don't match</li>
	 * </ul>
	 * If code == 400 ResponseEntity also has body containing errorCode:
	 * <ul>
	 *     <li>1 - username already exists</li>
	 *     <li>2 - passwords don't match</li>
	 *     <li>3 - both</li>
	 * </ul>
	 */
	@PostMapping("/auth/register")
	@CrossOrigin(origins = "*")
	public ResponseEntity register(@RequestParam String username, @RequestParam String password, @RequestParam String validatePassword) {
		int errorCode = 0;
		System.out.println(username + " " + password);
		if (RedisAccess.exists(username)) {
			errorCode = 1;
		}
		if (!password.equals(validatePassword)) {
			errorCode = errorCode == 0 ? 2 : 3;
		}
		if (errorCode == 0) {
			User u = new User();
			u.setPassword(password);
			u.setUsername(username);
			RedisAccess.save(u);
			return new ResponseEntity(HttpStatus.OK);
		} else {
			String body = "{errorCode: " + errorCode + "}";
			return new ResponseEntity(body, HttpStatus.BAD_REQUEST);
		}
	}
}
