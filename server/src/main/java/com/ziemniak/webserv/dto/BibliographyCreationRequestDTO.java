package com.ziemniak.webserv.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Żądanie utworzenia nowej bibliografii")
public class BibliographyCreationRequestDTO {
	@ApiModelProperty(notes = "Nazwa nowej bibliografii")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
