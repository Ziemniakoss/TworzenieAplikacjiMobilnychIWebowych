package com.ziemniak.webserv;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserNameCheck {
	private String message;
	private boolean available;

	public UserNameCheck() {
	}

	public UserNameCheck(String message, boolean avabile) {
		this.message = message;
		this.available = avabile;
	}

	public String getMessage() {
		return message;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}
}
