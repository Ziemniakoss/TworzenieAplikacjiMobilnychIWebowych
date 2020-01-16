package com.ziemniak.webserv.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "FileSharedForUser",
		description = "Inforamcja o pliku udostępnionym dla użytkownika razem z informacją o tym kto jest właścicielem")
public class FileSharedForUserDTO {
	@ApiModelProperty(notes = "Identyfikator pliku w bazie danych")
	private int id;
	@ApiModelProperty(notes = "Nazwa pliku")
	private String name;
	@ApiModelProperty(notes = "Data utworzenia pliku")
	private String creationDate;
	@ApiModelProperty(notes = "Nick właścicilea pliku")
	private String owner;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
}
