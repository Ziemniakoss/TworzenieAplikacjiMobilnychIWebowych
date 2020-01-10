package com.ziemniak.webserv.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "FileShareRequestDTO", description = "Żądanie udostępnienie danego pliku innemu użytkownikowi")
public class FileShareRequestDTO {
	@ApiModelProperty(name = "id", notes = "Identyfikator pliku do udostępnienia")
	private int fileId;
	@ApiModelProperty(notes = "Nazwa użytkownika któremu należy udosępnić plik")
	private String username;

	public int getFileId() {
		return fileId;
	}

	public void setFileId(int fileId) {
		this.fileId = fileId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
