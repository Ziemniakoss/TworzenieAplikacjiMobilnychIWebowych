package com.ziemniak.webcli.dto;

import com.ziemniak.webcli.utils.EqualFields;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


/**
 * Request for registering new user in service
 */
//todo sprawdzanie czy hasła się zgadzają
@EqualFields(first = "password", second = "validatePassword", message = "Passwords don't match")
public class RegisterRequestDTO {
	@NotBlank(message = "Username is required")
	@Size(min = 6, message = "Username must be between 6 and 20  character long")
	//@Pattern(regexp = "", message = "") todo brak znaków specjalnych
	private String username;

	@NotBlank(message = "Password is required")
	@Size(min = 7, message = "Password must be at least 7 characters long")
	private String password;

	@NotBlank(message = "Password validation is required")
	private String validatePassword;

	public RegisterRequestDTO() {
	}

	public RegisterRequestDTO(String username, String password, String validatePassword) {
		this.username = username;
		this.password = password;
		this.validatePassword = validatePassword;
	}

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

	@Override
	public String toString() {
		return "" +
				"username='" + username + '\'' +
				", password='" + password + '\'' +
				", validatePassword='" + validatePassword + '\''
				;
	}
}
