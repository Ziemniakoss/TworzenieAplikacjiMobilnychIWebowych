package com.ziemniak.webcli.dto;


public class LoginNegativeResponseDto {
	private String reason;

	public LoginNegativeResponseDto() {
	}

	public LoginNegativeResponseDto(String reason) {
		this.reason = reason;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}
