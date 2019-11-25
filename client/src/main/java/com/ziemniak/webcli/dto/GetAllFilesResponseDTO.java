package com.ziemniak.webcli.dto;

import com.ziemniak.webcli.File;

import java.util.List;

public class GetAllFilesResponseDTO {
	private List<File> files;

	public List<File> getFiles() {
		return files;
	}

	public void setFiles(List<File> files) {
		this.files = files;
	}
}
