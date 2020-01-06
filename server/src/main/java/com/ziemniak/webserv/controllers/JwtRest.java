package com.ziemniak.webserv.controllers;

import com.ziemniak.webserv.TokenManager;
import com.ziemniak.webserv.Useraaa;
import com.ziemniak.webserv.dto.*;
import com.ziemniak.webserv.repositories.BlacklistedJwtRepository;
import com.ziemniak.webserv.repositories.users.UserDoesNotExistException;
import com.ziemniak.webserv.repositories.users.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
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
	private TokenManager tokenManager;


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

