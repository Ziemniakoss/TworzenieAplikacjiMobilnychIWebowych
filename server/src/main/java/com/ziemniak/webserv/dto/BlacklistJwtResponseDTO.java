package com.ziemniak.webserv.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "BlackistJwtResponse", description = "Response for JWT blacklisting request ")
public class BlacklistJwtResponseDTO {
	@ApiModelProperty(notes = "Requested JWT to blacklist")
	private String jwt;
	@ApiModelProperty(notes = "Was it blacklisted")
	private boolean blacklisted;

	public BlacklistJwtResponseDTO(String jwt, boolean blacklisted) {
		this.jwt = jwt;
		this.blacklisted = blacklisted;
	}

	public String getJwt() {
		return jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}

	public boolean isBlacklisted() {
		return blacklisted;
	}

	public void setBlacklisted(boolean blacklisted) {
		this.blacklisted = blacklisted;
	}
}
