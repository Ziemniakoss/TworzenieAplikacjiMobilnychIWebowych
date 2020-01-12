package com.ziemniak.webserv.controllers;

import com.ziemniak.webserv.dto.CommonPasswordCheckResponseDTO;
import com.ziemniak.webserv.dto.UserNameCheckDTO;
import com.ziemniak.webserv.repositories.users.UserRepository;
import com.ziemniak.webserv.utils.PasswordUtils;
import com.ziemniak.webserv.utils.PasswordValidationException;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@Api(description = "Umożliwia na sprwadzanie dostępności haseł i nicków")
public class Checking {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordUtils passwordUtils;

	@GetMapping("/check/username/{username}")
	@CrossOrigin()
	@ApiOperation(value = "Sprawdza czy dana nazwa użytkownika jest poprawna", response = UserNameCheckDTO.class)
	@ApiResponses({
			@ApiResponse(code = 200, message = "Dostępna", response = UserNameCheckDTO.class),
			@ApiResponse(code = 406, message = "Niedostępna", response = UserNameCheckDTO.class)
	})
	@ApiParam(required = true, name = "username", value = "Username to check if is available")
	public ResponseEntity<UserNameCheckDTO> checkUsernameAvailability(@PathVariable(value = "username") String username) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Allow-Origin", "*");

		UserNameCheckDTO resonse;
		if (userRepository.userExists(username)) {
			resonse = new UserNameCheckDTO(username, false, "Użytkownik już istnieje");
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).header(String.valueOf(headers)).body(resonse);
		} else {
			resonse = new UserNameCheckDTO(username, true, "");
			return ResponseEntity.ok().header(String.valueOf(headers)).body(resonse);
		}
	}

	@GetMapping("/check/password/validate/{password}")
	@ApiOperation(value = "Pozwala na sprawdzenie jak silne jest hasło")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Hasło spełnia wymagania"),
			@ApiResponse(code = 400, message = "Hasło nie spełnia wymagań", response = List.class)
	})
	public ResponseEntity<?> validatePassword(@PathVariable String password) {
		try {
			passwordUtils.validate(password);
			return ResponseEntity.ok().build();
		} catch (PasswordValidationException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getErrors());
		}
	}

	@GetMapping("/check/password/isCommon/{password}")
	@ApiOperation(value = "Sprawdź czy hasło jest w bazie haseł popularnych(słabych)", produces = "application/json")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Hasło nie jest w rejestrze popularnych haseł", response = CommonPasswordCheckResponseDTO.class),
			@ApiResponse(code = 400,
					message = "Hasło jest w rejestrze popularnych haseł",
					response = CommonPasswordCheckResponseDTO.class,
					examples = @Example(
							value = {
							@ExampleProperty(mediaType = "application/json",
									value = "{\"password\":\"123456\",\"common\": true}")
					})//todo doprowadzic do dzialania

				)
	})
	public ResponseEntity<CommonPasswordCheckResponseDTO> checkIfIsCommon(@PathVariable String password) {
		if (passwordUtils.isWeak(password)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CommonPasswordCheckResponseDTO(password, true));
		} else {
			return ResponseEntity.ok(new CommonPasswordCheckResponseDTO(password, false));
		}
	}
}
