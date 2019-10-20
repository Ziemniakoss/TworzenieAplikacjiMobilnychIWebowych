package com.ziemniak.webcli;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClientApplication {
	public static final String URL_TO_SERVER="172.18.0.22:42069";

	public static void main(String[] args) {
		SpringApplication.run(ClientApplication.class, args);
	}

}
