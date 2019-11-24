package com.ziemniak.webcli.dto;

import javax.validation.constraints.NotBlank;

public class LoginRequestDTO {
	@NotBlank(message = "Please enter your username")
	private String username;
	@NotBlank(message = "Please enter password")
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
