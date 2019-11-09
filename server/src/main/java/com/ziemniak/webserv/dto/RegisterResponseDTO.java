package com.ziemniak.webserv.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(value = "RegisterResponse", description = "Response for request of making new user in database")
public class RegisterResponseDTO {
	@ApiModelProperty(notes = "Username that was requested to be created in database")
	private String username;
	@ApiModelProperty(notes = "Was it created?")
	private boolean accepted;
	@ApiModelProperty(notes = "List of errors if user was not created, for example \"Password is to short\"")
	private List<String> errors;

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

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
}
