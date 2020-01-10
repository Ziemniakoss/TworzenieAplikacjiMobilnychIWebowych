package com.ziemniak.webserv.repositories.bibliographies;

public class BibliographyDoesNotExistsException extends Exception {
	private final int id;

	public BibliographyDoesNotExistsException(int id) {
		super("Bibliography with id " + id + " does not exit");
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
