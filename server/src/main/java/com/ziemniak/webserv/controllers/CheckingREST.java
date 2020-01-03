package com.ziemniak.webserv;

import com.ziemniak.webserv.dto.UserNameCheckDTO;
import com.ziemniak.webserv.repositories.UserRepository;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Allows to chceck if given nick is available and how strong password is
 */
@RestController
@Api(description = "Checks if username is available or if password is strong enough")
public class CheckingREST {
	@Autowired
	private UserRepository userRepository;

	/**
	 * Check if given username is available
	 */
	@GetMapping("/check/username/{username}")
	@CrossOrigin()
	@ApiOperation(value = "Check if given username is available", response = UserNameCheckDTO.class)
	@ApiResponses({
			@ApiResponse(code = 200, message = "Username is available", response = UserNameCheckDTO.class),
			@ApiResponse(code = 406, message = "Username is not available", response = UserNameCheckDTO.class)
	})
	@ApiParam(required = true, name = "username", value = "Username to check if is available")
	public ResponseEntity<UserNameCheckDTO> checkUsernameAvailability(@PathVariable(value = "username") String username) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Allow-Origin", "*");

		UserNameCheckDTO resonse;
		if (userRepository.exists(username)) {
			resonse = new UserNameCheckDTO(username, false,"Not available");
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).header(String.valueOf(headers)).body(resonse);
		} else {
			resonse = new UserNameCheckDTO(username,true,"Already exists");
			return ResponseEntity.ok().header(String.valueOf(headers)).body(resonse);
		}
	}

	@GetMapping("/check/password/{password}")
	public ResponseEntity checkPasswordStrength(@PathVariable(value = "password") String password) {
		//TODO
		return null;
	}
}
