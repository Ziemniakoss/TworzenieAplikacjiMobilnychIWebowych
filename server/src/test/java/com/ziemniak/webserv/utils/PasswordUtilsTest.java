package com.ziemniak.webserv.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
//todo
class PasswordUtilsTest {
	@Autowired
	PasswordUtils ps;

	@ParameterizedTest
	@ValueSource(strings = {
			"hasL0aaa*",
			"aaaCaA0))0",
			"BarDz0MoCn3H@s10!",
	})

	void validateCorrectPasswords(String password) throws PasswordValidationException{
		//todo
	}

	@ParameterizedTest
	@ValueSource(strings = {
			"123456",
			"haslo",
			"",
			"alamakota"
	})
	void validateIncorrectPasswords(String password) {
		assertThrows(PasswordValidationException.class, ()->ps.getValidationErrors(password));
	}


	String[] generateTestData() {
		return new String[]{
				"aaa", "bbb"
		};
	}

}