package com.ziemniak.webcli.dto;

import javax.validation.constraints.NotBlank;

public class LoginRequestDTO {
	@NotBlank(message = "Proszę podać login")
	private String username;
	@NotBlank(message = "Proszę podać hasło")
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
