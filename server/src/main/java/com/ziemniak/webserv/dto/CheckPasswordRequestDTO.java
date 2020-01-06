package com.ziemniak.webserv.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "CheckPasswordRequest", description = "Żądanie sprawdzenia siły hasła")
public class CheckPasswordRequestDTO {
	@ApiModelProperty(notes = "Hasło do sprawdzenia")
	private String password;

	public CheckPasswordRequestDTO() {
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
