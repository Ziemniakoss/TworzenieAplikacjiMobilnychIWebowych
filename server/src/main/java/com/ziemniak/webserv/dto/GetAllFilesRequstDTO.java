package com.ziemniak.webserv.dto;

import com.ziemniak.webserv.repositories.files.FileInfo;

import java.util.List;

public class GetAllFilesRequstDTO {
	private List<FileInfo> fileInfos;

	public List<FileInfo> getFileInfos() {
		return fileInfos;
	}

	public void setFileInfos(List<FileInfo> fileInfos) {
		this.fileInfos = fileInfos;
	}
}
