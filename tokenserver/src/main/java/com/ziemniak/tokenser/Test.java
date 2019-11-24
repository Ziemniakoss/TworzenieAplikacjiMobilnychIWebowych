package com.ziemniak.tokenser;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash
public class Test {
	@Id
	private String id;
	private String name;
	private String surname;
	private String email;
}
