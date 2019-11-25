package com.ziemniak.webserv.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "FileNegativeResponse",description = "When request to return file was denied")
public class FileNegativeResponseDto {
	@ApiModelProperty(notes = "reason for which this request was denied")
	private String reason;

	public FileNegativeResponseDto(String reason) {
		this.reason = reason;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}
