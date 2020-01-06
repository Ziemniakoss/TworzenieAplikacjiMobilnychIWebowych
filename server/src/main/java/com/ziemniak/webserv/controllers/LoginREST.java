package com.ziemniak.webserv.controllers;

import com.ziemniak.webserv.TokenManager;
import com.ziemniak.webserv.dto.LoginNegativeResponseDTO;
import com.ziemniak.webserv.dto.LoginPositiveResponseDTO;
import com.ziemniak.webserv.dto.LoginRequestDTO;
import com.ziemniak.webserv.repositories.BlacklistedJwtRepository;
import com.ziemniak.webserv.repositories.users.UserRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

@RestController
public class LoginREST {
	private static final int JWT_DURABILITY = 5 * 1000 * 60;//5min
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TokenManager tokenManager;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostMapping(value = "/auth/login", produces = "application/json", consumes = "application/json")
	@ApiOperation(value = "Wytwarza token JWT dla podanego użytkownika", produces = "application/json", consumes = "application/json")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Podane dane do logowania były poprawne", response = LoginPositiveResponseDTO.class),
			@ApiResponse(code = 401, message = "Podane hasło lub login jest nieprawidłowe", response = LoginNegativeResponseDTO.class)
	})
	public ResponseEntity generateToken(@RequestBody LoginRequestDTO req) {
		try{//todo opznienia
			User user = userRepository.getUser(req.getUsername());
			if(passwordEncoder.matches(req.getPassword(),user.getPassword())){
				String jwt = tokenManager.create(req.getUsername(), new Date(System.currentTimeMillis() + JWT_DURABILITY));
				LoginPositiveResponseDTO resp = new LoginPositiveResponseDTO();
				resp.setJwt(jwt);
				return new ResponseEntity<>(resp, HttpStatus.OK);
			}
		}catch (UsernameNotFoundException ignored){
		}
		return new ResponseEntity<>(new LoginNegativeResponseDTO("Username or password is not correct"), HttpStatus.UNAUTHORIZED);
	}
}
