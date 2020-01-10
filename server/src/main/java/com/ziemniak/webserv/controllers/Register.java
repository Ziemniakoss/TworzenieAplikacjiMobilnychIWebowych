package com.ziemniak.webserv.controllers;

import com.ziemniak.webserv.dto.RegisterRequestDTO;
import com.ziemniak.webserv.dto.RegisterResponseDTO;
import com.ziemniak.webserv.repositories.users.UserAlreadyExistsException;
import com.ziemniak.webserv.repositories.users.UserRepository;
import com.ziemniak.webserv.utils.PasswordValidationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Controller responsible only for registering new users
 */
@RestController
@RequestMapping("/auth/register")
@CrossOrigin()
@Api(description = "Pozwala na rejstrowanie się")
public class Register {
	private final Logger log = LoggerFactory.getLogger(Register.class);
	@Autowired
	private UserRepository userRepository;


	@PostMapping(produces = "application/json", consumes = "application/json")
	@CrossOrigin(origins = "*")
	@ApiResponses({
			@ApiResponse(code = 200, message = "User was created", response = RegisterResponseDTO.class),
			@ApiResponse(code = 400,
					message = "Użytkownik nie został utworzony. Mogło to być spowodwane albo faktem, że " +
							"użytkownik z podaną nazwą już istnieje w bazie danych albo hasło nie spełniało norm",
					response = RegisterResponseDTO.class)
	})
	@ApiOperation(value = "Tworzy nowego użytkownika w bazie danych", produces = "application/json", consumes = "application/json")
	public ResponseEntity<RegisterResponseDTO> register(@RequestBody RegisterRequestDTO request) throws PasswordValidationException, UserAlreadyExistsException {
		boolean accepted = false;
		List<String> errors = new ArrayList<>();
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();//todo wrzucenie jako beana
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<RegisterRequestDTO>> violations = validator.validate(request);
		if (violations.size() == 0) {
			if (!userRepository.userExists(request.getUsername())) {
				userRepository.add(request.getUsername(),request.getPassword());//todo refactor
				log.info("Added new user with username '" + request.getUsername() + "'");
				accepted = true;
			} else {
				errors.add("User with username '" + request.getUsername() + "' already exists");
			}
		} else {
			for (ConstraintViolation<RegisterRequestDTO> viol : violations) {
				errors.add(viol.getMessage());
			}
		}

		RegisterResponseDTO body = new RegisterResponseDTO();
		body.setAccepted(accepted);
		body.setErrors(errors);
		body.setUsername(request.getUsername());
		if (accepted)
			return new ResponseEntity<>(body, HttpStatus.OK);
		else
			return new ResponseEntity(body, HttpStatus.BAD_REQUEST);
	}
}
