package com.ziemniak.webserv.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Negative response for login request", value = "NegativeLoginResponse")
public class LoginNegativeResponseDTO {
	@ApiModelProperty(notes = "Why was response rejected(bad login or password)")
	private String reason;

	public LoginNegativeResponseDTO() {
	}

	public LoginNegativeResponseDTO(String reason) {
		this.reason = reason;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}
