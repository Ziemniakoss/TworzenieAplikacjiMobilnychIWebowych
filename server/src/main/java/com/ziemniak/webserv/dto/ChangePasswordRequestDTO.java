package com.ziemniak.webserv.dto;

import com.ziemniak.webserv.utils.EqualFields;

@EqualFields(first = "newPassword",second = "validateNewPassword",message = "Hasła się nie zgadzają")
public class ChangePasswordRequestDTO {
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
