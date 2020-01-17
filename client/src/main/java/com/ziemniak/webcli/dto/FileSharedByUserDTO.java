package com.ziemniak.webcli.dto;


import java.util.List;

public class FileSharedByUserDTO {
	private int id;
	private String name;
	private String creationDate;
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

	@Override
	public String toString() {
		return "FileSharedByUserDTO{" +
				"id=" + id +
				", name='" + name + '\'' +
				", creationDate='" + creationDate + '\'' +
				", sharedTo=" + sharedTo +
				'}';
	}
}
