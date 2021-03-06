package com.ziemniak.webserv.utils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PasswordUtilsTest {
	@Autowired
	PasswordUtils ps;

	@ParameterizedTest
	@ValueSource(strings = {
			"hasL0aaa*",
			"aaaCaA0))0",
			"BarDz0MoCn3H@s10!",
	})

	void validateCorrectPasswords(String password) {
			assertDoesNotThrow(()->ps.validate(password));
	}

	@ParameterizedTest
	@ValueSource(strings = {
			"123456",
			"haslo",
			"",
			"alamakota"
	})
	void validateIncorrectPasswords(String password) {
		assertThrows(PasswordValidationException.class, ()->ps.validate(password));
	}


	String[] generateTestData() {
		return new String[]{
				"aaa", "bbb"
		};
	}

}