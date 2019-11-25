package com.ziemniak.webcli.dto;


public class BlacklistJwtRequestDTO {
	private String jwt;

	public BlacklistJwtRequestDTO(String jwt) {
		this.jwt = jwt;
	}

	public String getJwt() {
		return jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}
}
