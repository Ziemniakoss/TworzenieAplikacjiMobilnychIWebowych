package com.ziemniak.webserv;

import com.ziemniak.webserv.dto.RegisterRequestDTO;
import com.ziemniak.webserv.dto.RegisterResponseDTO;
import com.ziemniak.webserv.filestorage.StorageService;
import com.ziemniak.webserv.repositories.UserRepository;
import io.swagger.annotations.Api;
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
@Api(value = "Allows to create new user in database")
public class RegisterREST {
	private final Logger log = LoggerFactory.getLogger(RegisterREST.class);
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private StorageService storageService;

	@PostMapping(produces = "application/json", consumes = "application/json")
	@CrossOrigin(origins = "*")
	@ApiResponses({
			@ApiResponse(code = 200, message = "User was created", response = RegisterResponseDTO.class),
			@ApiResponse(code = 400,
					message = "User was not created. It might be caused by illegal password value, " +
							"not matching passwords or the fact that user with given username already exists in database",
					response = RegisterResponseDTO.class)
	})
	public ResponseEntity register(@RequestBody RegisterRequestDTO request) {
		boolean accepted = false;
		List<String> errors = new ArrayList<>();
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();//todo wrzucenie jako beana
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<RegisterRequestDTO>> violations = validator.validate(request);
		if (violations.size() == 0) {
			if (!userRepository.exists(request.getUsername())) {
				User u = new User();
				u.setPassword(request.getPassword());
				u.setUsername(request.getUsername());
				userRepository.save(u);
				log.info("Added new user with username \'" + u.getUsername() + "\'");
				accepted = true;
				storageService.addUser(u.getUsername());
			} else {
				errors.add("User with username \'" + request.getUsername() + "\' already exists");
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
			return new ResponseEntity(body, HttpStatus.OK);
		else
			return new ResponseEntity(body, HttpStatus.BAD_REQUEST);
	}
}
