package com.ziemniak.webcli.models;

public class MChangePassword {
	private String oldPassword;
	private String newPassword;
	private String validateNewPassword;

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getValidateNewPassword() {
		return validateNewPassword;
	}

	public void setValidateNewPassword(String validateNewPassword) {
		this.validateNewPassword = validateNewPassword;
	}
}
