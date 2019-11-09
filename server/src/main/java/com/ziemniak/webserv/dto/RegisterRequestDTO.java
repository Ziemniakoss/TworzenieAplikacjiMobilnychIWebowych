package com.ziemniak.webserv.dto;

import com.ziemiakoss.utils.validation.EqualFields;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@ApiModel(description = "Model containg request to create user with given username and password is database",value = "RegisterRequest")
@EqualFields(first = "password", second = "validatePassword", message = "Passwords don't match")
public class RegisterRequestDTO {
	@NotBlank
	@Size(min = 6, message = "Username must be between 6 and 20  character long")
	@ApiModelProperty(notes = "Name for new user")
	private String username;

	@ApiModelProperty(notes="Password for new user. Must be at least 7 character long")
	@NotBlank(message = "Password is required")
	@Size(min = 7, message = "Password must be at least 7 characters long")
	private String password;

	@ApiModelProperty(notes = "Validation of password. It must have same value as password")
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
