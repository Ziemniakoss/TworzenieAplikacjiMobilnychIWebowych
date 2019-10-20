package com.ziemniak.webserv;

import com.ziemniak.webserv.redis.RedisAccess;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Allows to chceck if given nick is available and how strong password is
 */
@Controller
public class CChecks {

	@GetMapping("/check/username/{username}")
	public ResponseEntity checkUsernameAvailability(@PathVariable(value = "username") String username){
		ResponseEntity resonse = new ResponseEntity(HttpStatus.OK);
		String body = null;
		if(RedisAccess.exists(username)){
			body = "{available: false, message: \"Username already exists\"}";
		}else{
			body = "{available: true, message: \"ok\"}";
		}
		return new ResponseEntity(body, HttpStatus.OK);
	}

	@GetMapping("/check/password/{password}")
	public ResponseEntity checkPasswordStrength(@PathVariable(value = "password")String password){
		//TODO
		return null;
	}
}
