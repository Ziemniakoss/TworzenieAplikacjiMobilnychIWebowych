package com.ziemniak.webcli;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClientApplication {
	public static  String URL_TO_SERVER="http://localhost:42069";// "https://ziemback.herokuapp.com";

	public static void main(String[] args) {

		SpringApplication.run(ClientApplication.class, args);
	}
}
