package com.ziemniak.webserv.filestorage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class File {
	private static final Logger log = LoggerFactory.getLogger(File.class);
	private String name;
	private String id;
	private String extension;
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


	public File() {
	}

	public File(String name) {
		setName(name);
	}

	public File(String id, String name) {
		setName(name);
		setId(id);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		int index = name.lastIndexOf('.');
		if (index == -1 || index == name.length() - 1) {
			this.extension = getExtensionName("");
		} else {
			String ex = name.substring(Math.min(name.lastIndexOf('.') + 1, name.length() - 1));
			this.extension = getExtensionName(ex);
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getExtension() {
		return extension;
	}

	private static String getExtensionName(String extension) {
		switch (extension) {
			case "pdf":
				return "PDF document";
			case "":
				return "No extension";
			case "docs":
				return "Word document";
			case "html":
				return "HTML file";
			case "txt":
				return "Text document";
			case "odt":
				return "OpenDocument";
			default:
				log.warn("Unknown extension of file: " + extension);
				return "Unknown extension";
		}
	}
}
