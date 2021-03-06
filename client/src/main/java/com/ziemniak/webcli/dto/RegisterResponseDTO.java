package com.ziemniak.webcli.dto;

import java.util.List;

public class RegisterResponseDTO {
	private String username;
	private boolean accepted;
	private String[] errors;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isAccepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}

	public String[] getErrors() {
		return errors;
	}

	public void setErrors(String[] errors) {
		this.errors = errors;
	}
}
