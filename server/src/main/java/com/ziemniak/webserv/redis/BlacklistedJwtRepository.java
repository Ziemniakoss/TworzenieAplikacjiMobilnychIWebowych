package com.ziemniak.webserv.redis;

import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

@Repository
public class BlacklistedJwtRepository {
	private final String prefix = "blackList";

	public void add(String jwt) {
		Jedis j = new Jedis();
		j.set(prefix + jwt, "");
		j.close();
	}

	public boolean isBlacklisted(String jwt){
		Jedis j = new Jedis();
		boolean result = j.exists(prefix+jwt);
		j.close();
		return result;
	}
}
