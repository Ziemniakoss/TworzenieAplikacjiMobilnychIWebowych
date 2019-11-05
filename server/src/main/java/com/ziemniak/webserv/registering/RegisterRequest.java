package com.ziemniak.webserv.registering;

import com.ziemiakoss.utils.validation.EqualFields;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@EqualFields(first = "password", second = "validatePassword", message = "Passwords don't match")
public class RegisterRequest {
	@NotBlank
	@Size(min = 6, message = "Username must be between 6 and 20  character long")
	private String username;
	@NotBlank(message = "Password is required")
	@Size(min = 7, message = "Password must be at least 7 characters long")
	private String password;
	@NotBlank(message = "Password validation required")
	private String validatePassword;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getValidatePassword() {
		return validatePassword;
	}

	public void setValidatePassword(String validatePassword) {
		this.validatePassword = validatePassword;
	}
}
