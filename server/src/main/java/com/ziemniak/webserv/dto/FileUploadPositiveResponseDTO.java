package com.ziemniak.webserv.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "FileUploadPositiveResponse",description = "Response to successful file upload")
public class FileUploadPositiveResponseDTO {
	@ApiModelProperty(notes = "Uploaded file name")
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
