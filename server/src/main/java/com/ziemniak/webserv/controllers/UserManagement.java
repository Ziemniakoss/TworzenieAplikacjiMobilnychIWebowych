package com.ziemniak.webserv.controllers;

import com.ziemniak.webserv.dto.ChangePasswordRequestDTO;
import com.ziemniak.webserv.repositories.users.UserDoesNotExistException;
import com.ziemniak.webserv.repositories.users.UserRepository;
import com.ziemniak.webserv.utils.JwtUtils;
import com.ziemniak.webserv.utils.PasswordUtils;
import com.ziemniak.webserv.utils.PasswordValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserManagement {
	//todo przeniesc tutaj rejestacje i logowanie
	private UserRepository userRepository;
	private final JwtUtils jwtUtils;
	@Autowired
	private final PasswordUtils passwordUtils;

	@Autowired
	public UserManagement(UserRepository userRepository, JwtUtils jwtUtils, PasswordUtils passwordUtils) {
		this.userRepository = userRepository;
		this.jwtUtils = jwtUtils;
		this.passwordUtils = passwordUtils;
	}

	@PostMapping("/users/changepassword")
	public ResponseEntity updatePassword(@RequestBody ChangePasswordRequestDTO requestDTO, HttpServletRequest request) {
		String username = jwtUtils.extractUsername(request);
		User user = userRepository.getUser(username);

		try {
			userRepository.changePassword(user,requestDTO.getOldPassword(), requestDTO.getNewPassword(), requestDTO.getValidateNewPassword());
			return ResponseEntity.ok("Zmieniono");
		} catch (PasswordValidationException e) {
			return ResponseEntity.badRequest().body(e.getErrors());
		}
	}
}
