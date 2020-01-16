package com.ziemniak.webserv.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value ="RevokeAccessToFileRequest", description = "Żądanie cofnięcia uprwanieni do danego pliku dla podanego użytkownika")
public class RevokeAccessToFileRequestDTO {
	@ApiModelProperty(notes = "Identyfikator pliku")
	private int fileId;
	@ApiModelProperty(notes = "Użytkownik któremu chcemy cofnąć uprawniwnienia")
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

	@Override
	public String toString() {
		return "RevokeAccessToFileRequestDTO{" +
				"fileId=" + fileId +
				", username='" + username + '\'' +
				'}';
	}
}
