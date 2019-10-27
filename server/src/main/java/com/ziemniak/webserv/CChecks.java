package com.ziemniak.webserv;

import com.ziemniak.webserv.redis.RedisAccess;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Allows to chceck if given nick is available and how strong password is
 */
@RestController
@CrossOrigin(origins = "*")
public class CChecks {

	@GetMapping("/check/username/{username}")
	@CrossOrigin()
	public ResponseEntity<UserNameCheck> checkUsernameAvailability(@PathVariable(value = "username") String username){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Allow-Origin", "*");

		UserNameCheck resonse;
		if(RedisAccess.exists(username)){
			resonse = new UserNameCheck("Not avalible", false);
		}else{
			resonse = new UserNameCheck("Avalible", true);
		}
		return ResponseEntity.ok().header(String.valueOf(headers)).body(resonse);
	}

	@GetMapping("/check/password/{password}")
	public ResponseEntity checkPasswordStrength(@PathVariable(value = "password")String password){
		//TODO
		return null;
	}
}
