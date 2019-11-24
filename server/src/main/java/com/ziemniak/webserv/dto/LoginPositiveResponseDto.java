package com.ziemniak.webserv.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Positive response to login request", value = "PositiveLoginResponse")
public class LoginPositiveResponseDto {
	@ApiModelProperty(notes = "JWT generated based on given login data")
	private String jwt;

	public String getJwt() {
		return jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}
}
