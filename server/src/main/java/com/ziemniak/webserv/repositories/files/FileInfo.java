package com.ziemniak.webserv.repositories.files;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class FileInfo {
	private String name;
	private int id;
	private String creationDate;

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public void setCreationDate(LocalDateTime date) {
		//dd.m.yyyy h:m
		String formatted = String.format("%d.%d.%d %d:%d", date.getDayOfMonth(), date.getMonth().getValue(), date.getYear(),
				date.getHour(), date.getMinute());
		creationDate = formatted;
	}


	public FileInfo() {
	}

	public FileInfo(String name) {
		setName(name);
	}

	public FileInfo(int id, String name) {
		setName(name);
		setId(id);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCreationDate(Timestamp timestamp) {
		setCreationDate(timestamp.toLocalDateTime());
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
