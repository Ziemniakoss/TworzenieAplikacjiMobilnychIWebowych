package com.ziemniak.webserv.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(value = "FileShardByUser", description = "Informacja o pliku zawierająca dane o tym komu został udostępniony")
public class FileSharedByUserDTO {
	@ApiModelProperty(notes = "Id pliku w bazie danych")
	private int id;
	@ApiModelProperty("Nazwa pliku w bazie danych")
	private String name;
	@ApiModelProperty("Data utworzenia pliku")
	private String creationDate;
	@ApiModelProperty("Lista nicków użytkowników którzy mają dostęp do tego pliku poza właścicielem")
	private List<String> sharedTo;

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

	public List<String> getSharedTo() {
		return sharedTo;
	}

	public void setSharedTo(List<String> sharedTo) {
		this.sharedTo = sharedTo;
	}
}
