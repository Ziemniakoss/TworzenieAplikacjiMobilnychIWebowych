package com.ziemniak.webserv.dto;

import com.ziemniak.webserv.utils.EqualFields;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@ApiModel(description = "Model containg request to create user with given username and password is database", value = "RegisterRequest")
@EqualFields(first = "password", second = "validatePassword", message = "Hasła się nie zgadzają")
public class RegisterRequestDTO {
	@NotBlank(message = "Nazwa użytkownika nie może być pusta")
	@Size(min = 6, max = 30, message = "Nazwa użytkownika musi mieć od 6 do 30 znaków")//todo czy na pewno
	private String username;

	@NotBlank(message = "Proszę podać hasło")
	@Size(min = 6, message = "Hasło musi mieć co najmniej 6 znaków")
	@Pattern(regexp = ".*\\p{javaUpperCase}.*", message = "Hasło musi zawierać przynajmniej jedną wielką literę")
	@Pattern(regexp = ".*\\p{javaDigit}.*", message = "Hasło musi zawierać przynajmniej jedną cyfrę")
	@Pattern(regexp = ".*\\p{javaLowerCase}.*", message = "Hasło musi zawierać przynajmniej jedną małą literę")
	private String password;

	@NotBlank(message = "Proszę potwierdzić hasło")
	private String validatePassword;

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
}
