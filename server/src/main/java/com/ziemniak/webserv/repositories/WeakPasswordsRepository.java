package com.ziemniak.webserv.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class WeakPasswordsRepository {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public boolean isRegistered(String password) {
		if (password == null) {
			return false;
		}
		return jdbcTemplate.queryForObject(
				"SELECT EXISTS(SELECT 'a' FROM weak_passwords WHERE password = ?);",
				Boolean.class, password);
	}
}
