package com.ziemniak.webserv;

import com.ziemniak.webserv.dto.*;
import com.ziemniak.webserv.repositories.BlacklistedJwtRepository;
import com.ziemniak.webserv.repositories.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Pozwala na wytwarzanie JWT
 */
@RestController
@Api(description = "JWT Authentication", value = "Allows creating and blacklisting JWT")
public class JwtRest {
	private final Logger log = LoggerFactory.getLogger(JwtRest.class);
	private static final int JWT_DURABILITY = 5 * 1000 * 60;//5min
	@Autowired
	private BlacklistedJwtRepository blackList;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TokenManager tokenManager;

	@PostMapping(value = "/auth/login", produces = "application/json", consumes = "application/json")
	@ApiOperation(value = "Login with username and password and receive jwt", produces = "application/json", consumes = "application/json")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Login was successful", response = LoginPositiveResponseDTO.class),
			@ApiResponse(code = 401, message = "Given password or login was not correct", response = LoginNegativeResponseDTO.class)
	})
	public ResponseEntity createAuthorizationToken(@RequestBody LoginRequestDTO req) {
		if (userRepository.exists(req.getUsername())) {
			User u = userRepository.getUser(req.getUsername());
			if (u.getPassword().equals(req.getPassword())) {
				String jwt = tokenManager.create(req.getUsername(), new Date(System.currentTimeMillis() + JWT_DURABILITY));
				LoginPositiveResponseDTO resp = new LoginPositiveResponseDTO();
				resp.setJwt(jwt);
				return new ResponseEntity<>(resp, HttpStatus.OK);
			}
			return new ResponseEntity<>(new LoginNegativeResponseDTO("Wrong password"), HttpStatus.UNAUTHORIZED);
		}
		return new ResponseEntity<>(new LoginNegativeResponseDTO("User does not exists"), HttpStatus.UNAUTHORIZED);
	}

	@PostMapping(value = "/auth/logout", produces = "application/json", consumes = "application/json")
	@ApiOperation(value = "Blacklist JWT", produces = "application/json", consumes = "application/json")
	@ApiResponse(code = 200, message = "Successfully blaclisted jwt", response = BlacklistJwtResponseDTO.class)
	public ResponseEntity blacklistJwt(@RequestBody BlacklistJwtRequestDTO req) {
		if (tokenManager.verify(req.getJwt())) {
			blackList.add(req.getJwt());
		}
		log.info("JWT was blacklisted(" + req.getJwt() + ")");
		return new ResponseEntity<>(new BlacklistJwtResponseDTO(req.getJwt(), true), HttpStatus.OK);
	}
}

