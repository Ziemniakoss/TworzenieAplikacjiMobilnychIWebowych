package com.ziemniak.webcli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class File {
	private static final Logger log = LoggerFactory.getLogger(File.class);
	private String name;
	private String id;
	private String extension;


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
		System.out.println(extension);
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

	public void setExtension(String extension) {
		//Specjalne puste body
	}
}
