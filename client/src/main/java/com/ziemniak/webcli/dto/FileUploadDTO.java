package com.ziemniak.webcli.dto;

public class FileUploadDTO {
	private String name;
	private byte [] file;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	public FileUploadDTO(String name, byte[] file) {
		this.name = name;
		this.file = file;
	}
}
