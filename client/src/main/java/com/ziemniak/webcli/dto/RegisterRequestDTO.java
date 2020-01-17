package com.ziemniak.webcli.dto;

import com.ziemniak.webcli.utils.EqualFields;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


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
	@Pattern(regexp = "[^[!@#$%^&*()<>:\";']]*", message = "Wiadomość nie może zawrierać znaków specjalnych !@#$%^&*()<>:\";'")
	private String password;

	@NotBlank(message = "Proszę potwierdzić hasło")
	private String validatePassword;

	public RegisterRequestDTO() {
	}

	public RegisterRequestDTO(String username, String password, String validatePassword) {
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
