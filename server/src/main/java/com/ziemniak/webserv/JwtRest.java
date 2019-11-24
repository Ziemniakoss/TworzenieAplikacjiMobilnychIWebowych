package com.ziemniak.webserv;

import com.ziemniak.webserv.dto.LoginNegativeResponseDto;
import com.ziemniak.webserv.dto.LoginPositiveResponseDto;
import com.ziemniak.webserv.dto.LoginRequestDto;
import com.ziemniak.webserv.repositories.BlacklistedJwtRepository;
import com.ziemniak.webserv.repositories.UserRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
/**
 * Pozwala na wytwarzanie JWT
 */
public class JwtRest {
	private static final int JWT_DURABILITY = 5 * 1000 * 60;//5min
	@Autowired
	private BlacklistedJwtRepository blackList;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TokenManager tokenManager;

	@PostMapping(value = "/auth/login", produces = "application/json",consumes = "application/json")
	@ApiOperation(value = "Login with username and password and receive jwt ")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Login was successful", response = LoginPositiveResponseDto.class),
			@ApiResponse(code = 401, message = "Given password or login was not correct", response = LoginNegativeResponseDto.class)
	})
	public ResponseEntity createAuthorizationToken(@RequestBody LoginRequestDto req) {
		if (userRepository.exists(req.getUsername())) {
			User u = userRepository.getUser(req.getUsername());
			if (u.getPassword().equals(req.getPassword())) {
				String jwt = tokenManager.create(req.getUsername(), new Date(System.currentTimeMillis() + JWT_DURABILITY));
				LoginPositiveResponseDto resp = new LoginPositiveResponseDto();
				resp.setJwt(jwt);
				return new ResponseEntity<>(resp, HttpStatus.OK);
			}
			return new ResponseEntity<>(new LoginNegativeResponseDto("Wrong password"), HttpStatus.UNAUTHORIZED);
		}
		return new ResponseEntity<>(new LoginNegativeResponseDto("User does not exists"), HttpStatus.UNAUTHORIZED);
	}

	@PostMapping(value = "/auth/logout",produces = "application/json",consumes = "application/json")
	public String blacklistJwt(String jwt) {
		if (tokenManager.verify(jwt)) {
			blackList.add(jwt);
		}
		return null;
	}
}
