package com.ziemniak.webserv.repositories;

import com.ziemniak.webserv.User;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

import java.util.Map;

@Repository
public  class UserRepository {
	private static final String PREFIX = "user:";

	public boolean exists(String nick) {
		Jedis j = new Jedis();
		boolean result = j.exists(PREFIX + nick);
		j.close();
		return result;
	}

	public void save(User user) {
		if (user == null) {
			throw new NullPointerException();
		}
		Jedis j = new Jedis();
		String key = PREFIX + user.getUsername();
		j.hset(key, "password", user.getPassword());
		j.close();
	}

	public User getUser(String nick) {
		if (nick == null)
			return null;
		Jedis j = new Jedis();
		Map<String, String> stringStringMap = j.hgetAll(PREFIX + nick);
		j.close();
		if (stringStringMap.isEmpty()) {
			return null;
		}
		User user = new User();
		user.setUsername(nick);
		user.setPassword(stringStringMap.get("password"));
		return user;
	}

	public void delete(User user) {
		if (user == null) {
			return;
		}
		Jedis j = new Jedis();
		j.del(user.getUsername());
		j.close();
	}
}