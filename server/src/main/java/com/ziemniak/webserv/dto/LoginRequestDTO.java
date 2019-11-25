package com.ziemniak.webserv.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

@ApiModel(description = "Request to login and generate JWT", value = "LoginRequest")
public class LoginRequestDTO {
	@NotBlank
	@ApiModelProperty(notes = "Username")
	private String username;
	@NotBlank
	@ApiModelProperty(notes = "Password")
	private String password;

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
}
