package com.ziemniak.webcli.dto;

public class BlacklistJwtResponseDTO {
	private String jwt;
	private boolean blacklisted;

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
