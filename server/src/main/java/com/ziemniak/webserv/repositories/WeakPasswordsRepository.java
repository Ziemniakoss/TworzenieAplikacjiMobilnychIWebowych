package com.ziemniak.webserv.repositories;

import org.springframework.stereotype.Repository;

@Repository
public class WeakPasswordsRepository {
	public boolean isRegistered(String password){
		if(password == null){
			return false;
		}
		//todo
		return "123456".equals(password);
	}
}
