package com.ziemniak.webserv.repositories.files;

public class FileDoesNotExistException extends FileRepositoryException {
	private final int id;

	public FileDoesNotExistException(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
