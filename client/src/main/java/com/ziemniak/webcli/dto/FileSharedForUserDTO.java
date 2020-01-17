package com.ziemniak.webcli.dto;

public class FileSharedForUserDTO {
	private int id;
	private String name;
	private String creationDate;
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

	@Override
	public String toString() {
		return "FileSharedForUserDTO{" +
				"id=" + id +
				", name='" + name + '\'' +
				", creationDate='" + creationDate + '\'' +
				", owner='" + owner + '\'' +
				'}';
	}
}
