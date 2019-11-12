package com.ziemniak.webcli;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClientApplication {
	public static  String URL_TO_SERVER="http://biblioziem-server:42069";

	public static void main(String[] args) {
		if(args.length > 0) {
			URL_TO_SERVER = args[0];
			System.out.println(URL_TO_SERVER);
		}
		SpringApplication.run(ClientApplication.class, args);
	}
}
