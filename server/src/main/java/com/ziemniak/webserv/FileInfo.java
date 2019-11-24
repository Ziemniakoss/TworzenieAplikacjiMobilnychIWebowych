package com.ziemniak.webserv;

import java.util.Date;

public class FileInfo {
	private final String id;
	private String name;
	private final String owner;
	private Date creationDate;

	public FileInfo(String id, String name, String owner, Date creationDate) {
		this.id = id;
		this.name = name;
		this.creationDate = creationDate;
		this.owner = owner;
	}

	public FileInfo(String name, String owner) {
		id = null;
		this.name = name;
		this.owner = owner;
		creationDate = new Date();
	}
}
