package com.ziemniak.webcli;

public class RegisterRequest {
	private String username;
	private String password;
	private String validatePassword;

	public RegisterRequest(String username, String password, String validatePassword) {
		this.username = username;
		this.password = password;
		this.validatePassword = validatePassword;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getValidatePassword() {
		return validatePassword;
	}

	public void setValidatePassword(String validatePassword) {
		this.validatePassword = validatePassword;
	}

	@Override
	public String toString() {
		return "" +
				"username='" + username + '\'' +
				", password='" + password + '\'' +
				", validatePassword='" + validatePassword + '\''
				;
	}
}
