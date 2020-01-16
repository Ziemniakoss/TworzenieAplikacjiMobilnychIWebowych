package com.ziemniak.webserv.dto;

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
}
