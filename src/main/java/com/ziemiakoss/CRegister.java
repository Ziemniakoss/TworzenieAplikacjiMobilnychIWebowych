package com.ziemiakoss;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CRegister {
	private static final int MIN_PASSWORD_LENGTH = 3;
	@RequestMapping(value = "/",method = {RequestMethod.GET})
	public String register(){
		return "register";
	}

	@RequestMapping(value = "/",method = {RequestMethod.POST})
	public String registerPost(@RequestParam("username")String username , @RequestParam("password")String password, @RequestParam("validatePassword") String validatePassword){
		boolean hasErrors = false;
		if(username.length() == 0){
			//todo
		}
		//todo czy username juz nie istnieje
		//todo czy username nie zawiera niedozwolonych znakow
		if(password.length() < MIN_PASSWORD_LENGTH){
			//todo
		}
		if(!password.equals(validatePassword)){
			// TODO: 12.10.2019
		}
		return "register";
	}
}
