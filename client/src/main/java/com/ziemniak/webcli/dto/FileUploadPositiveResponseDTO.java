package com.ziemniak.webcli.dto;

public class FileUploadPositiveResponseDTO {
	private String fileName;

	public String getFileName() {
		return fileName;
	}

	public FileUploadPositiveResponseDTO(String fileName) {
		this.fileName = fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
