package com.ziemniak.webserv.repositories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

@Repository
public class BlacklistedJwtRepository {
	private final Logger log = LoggerFactory.getLogger(BlacklistedJwtRepository.class);
	private final String prefix = "blackList";

	public void add(String jwt) {
		log.debug("Adding new jwt to blacklist");
		Jedis j = new Jedis();
		j.set(prefix + jwt, "");
		j.close();
	}

	public boolean isBlacklisted(String jwt){
		log.debug("checking if jwt is blacklisted");
		Jedis j = new Jedis();
		boolean result = j.exists(prefix+jwt);
		j.close();
		return result;
	}
}
