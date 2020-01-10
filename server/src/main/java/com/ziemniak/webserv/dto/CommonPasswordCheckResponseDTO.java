package com.ziemniak.webserv.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "CommonPasswordCheckResponse",
		description = "Odpowiedź na żądanie sprawdzenia czy hasło jest w rejestrze popularnych haseł")
public class CommonPasswordCheckResponseDTO {
	@ApiModelProperty(notes = "Hasło do sprawdzenia")
	private final String password;
	@ApiModelProperty(notes = "Czy hasło jest w rejestrze haseł słabych")
	private final boolean common;

	public CommonPasswordCheckResponseDTO(String password, boolean common) {
		this.password = password;
		this.common = common;
	}

	public String getPassword() {
		return password;
	}

	public boolean isCommon() {
		return common;
	}
}
