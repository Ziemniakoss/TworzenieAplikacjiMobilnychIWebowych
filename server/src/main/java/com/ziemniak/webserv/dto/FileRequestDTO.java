package com.ziemniak.webserv.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel(description = "Request to send file with given id", value = "FileRequest")
public class FileRequestDTO {
	@ApiModelProperty(notes = "Requested file's id")
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
