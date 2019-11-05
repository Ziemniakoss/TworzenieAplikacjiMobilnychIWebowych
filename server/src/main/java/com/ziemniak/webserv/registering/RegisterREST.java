package com.ziemniak.webserv.registering;

import com.ziemniak.webserv.redis.RedisAccess;
import com.ziemniak.webserv.redis.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@CrossOrigin(origins = "*")
public class RegisterREST {
	private final Logger log = LoggerFactory.getLogger(RegisterREST.class);

	@PostMapping(produces = "application/json", consumes = "application/json")
	@CrossOrigin(origins = "*")
	public ResponseEntity register(@RequestBody RegisterRequest request) {
		boolean accepted = false;
		List<String> errors = new ArrayList<>();
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();//todo wrzucenie jako beana
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
		if (violations.size() == 0) {
			if (!RedisAccess.exists(request.getUsername())) {
				User u = new User();
				u.setPassword(request.getPassword());
				u.setUsername(request.getUsername());
				RedisAccess.save(u);
				log.info("Added new user with username \'" + u.getUsername() + "\'");
				accepted = true;
			} else {
				errors.add("User with username \'" + request.getUsername() + "\' already exists");
			}
		} else {
			for (ConstraintViolation<RegisterRequest> viol : violations) {
				errors.add(viol.getMessage());
			}
		}

		RegisterResponse body = new RegisterResponse();
		body.setAccepted(accepted);
		body.setErrors(errors);
		body.setUsername(request.getUsername());
		if (accepted)
			return new ResponseEntity(body, HttpStatus.OK);
		else
			return new ResponseEntity(body, HttpStatus.BAD_REQUEST);
	}
}
