package com.ziemniak.webserv.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonIgnoreProperties(ignoreUnknown = true)

@ApiModel(value = "UserNameCheck")
public class UserNameCheckDTO {
	@ApiModelProperty(notes = "Username for which checking was performed")
	private String username;
	@ApiModelProperty(notes = "Is this username available?")
	private boolean available;
	@ApiModelProperty(notes = "Errors if username was not available, for example \"Already exists\"")
	private String message;

	public UserNameCheckDTO() {
	}

	public UserNameCheckDTO(String username, boolean avabile, String message) {
		this.username = username;
		this.message = message;
		this.available = avabile;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUsername() {
		return username;
	}

	public boolean isAvailable() {
		return available;
	}

	public String getMessage() {
		return message;
	}
}
