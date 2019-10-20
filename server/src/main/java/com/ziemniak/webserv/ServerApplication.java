package com.ziemniak.webserv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServerApplication {
	public static final String CLIENT = "http://localhost:8080";

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

}
