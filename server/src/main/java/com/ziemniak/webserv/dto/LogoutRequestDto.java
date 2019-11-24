package com.ziemniak.webserv.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Request to blacklist JWT", value = "LogoutRequest")
public class LogoutRequestDto {
	@ApiModelProperty(notes = "JWT to blacklist")
	private String jwt;

	public String getJwt() {
		return jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}
}
