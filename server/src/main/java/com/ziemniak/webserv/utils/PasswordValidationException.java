package com.ziemniak.webserv.utils;

import java.util.Collection;

public class PasswordValidationException extends Exception{
	private final double strength;
	private final Collection<String> errors;

	public PasswordValidationException(double strength, Collection<String> errors) {
		this.strength = strength;
		this.errors = errors;
	}

	public double getStrength() {
		return strength;
	}

	public Collection<String> getErrors() {
		return errors;
	}

	@Override
	public String toString() {
		return errors.toString();
	}
}
