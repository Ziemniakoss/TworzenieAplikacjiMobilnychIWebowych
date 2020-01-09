package com.ziemniak.webserv.repositories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BlacklistedJwtRepository {
	private final Logger log = LoggerFactory.getLogger(BlacklistedJwtRepository.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void blacklist(String jwt) {
		if (jwt == null) {
			return;
		}
		try {
			jdbcTemplate.update("INSERT INTO blacklisted_jwt (jwt) VALUES (?);", jwt);
			log.info(String.format("Blacklisted jwt (%s)", jwt));
		}catch (DataAccessException e){
			//prawdopodobnie jwt jest juz zbanowany
			log.error(String.format("Error(%s) occured while blaklisting jwt(%s): %s",e.getClass(),jwt,e.getMessage()));
		}
	}

	public boolean isBlacklisted(String jwt) {
		return jdbcTemplate.queryForObject(
				"SELECT EXISTS(SELECT NULL FROM blacklisted_jwt WHERE jwt = ?);", Boolean.class, jwt);
	}
}
