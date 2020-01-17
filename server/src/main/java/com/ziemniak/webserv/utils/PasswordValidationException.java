package com.ziemniak.webserv.utils;

import java.util.Collection;

public class PasswordValidationException extends Exception {
	private final Collection<String> errors;

	public PasswordValidationException(Collection<String> errors) {
		this.errors = errors;
	}

	public Collection<String> getErrors() {
		return errors;
	}

	@Override
	public String toString() {
		return errors.toString();
	}
}
