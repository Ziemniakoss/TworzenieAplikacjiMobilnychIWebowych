package com.ziemniak.webserv.dto;

import com.ziemniak.webserv.filestorage.File;

import java.util.List;

public class GetAllFilesRequstDTO {
	private List<File> files;

	public List<File> getFiles() {
		return files;
	}

	public void setFiles(List<File> files) {
		this.files = files;
	}
}
