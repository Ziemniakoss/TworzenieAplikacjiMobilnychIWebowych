package com.ziemniak.webcli.dto;

public class ShareFileRequestDTO {
	private int fileId;
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
		return "ShareFileRequestDTO{" +
				"fileId=" + fileId +
				", username='" + username + '\'' +
				'}';
	}
}
