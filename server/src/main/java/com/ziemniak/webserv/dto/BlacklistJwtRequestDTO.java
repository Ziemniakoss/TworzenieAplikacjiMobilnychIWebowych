package com.ziemniak.webserv.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "BlacklistJwtRequest", description = "Request to blacklist valid jwt")
public class BlacklistJwtRequestDTO {
	@ApiModelProperty(notes = "JWT to blacklist")
	private String jwt;

	public String getJwt() {
		return jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}
}
