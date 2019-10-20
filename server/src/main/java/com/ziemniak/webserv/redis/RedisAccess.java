package com.ziemniak.webserv.redis;

import redis.clients.jedis.Jedis;

import java.util.Map;


//TODO replace with CrudeRepository
public abstract class RedisAccess {
    private static final String PREFIX = "user:";

    public static boolean exists(String nick) {
        Jedis j = new Jedis();
        boolean result = j.exists(PREFIX + nick);
        j.close();
        return result;
    }

    public static void save(User user) {
        if (user == null) {
            throw new NullPointerException();
        }
        Jedis j = new Jedis();
        String key = PREFIX + user.getUsername();
        j.hset(key, "password", user.getPassword());
        j.close();
    }

    /**
     * Fetches user data from db
     *
     * @param nick nick of user
     * @return User from db or null if either nick was null or there was no user
     * with given nick
     */
    public static User getUser(String nick) {
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

    public void delete(User user){
        if(user == null){
            return;
        }
        Jedis j = new Jedis();
        j.del(user.getUsername());
        j.close();
    }


}
