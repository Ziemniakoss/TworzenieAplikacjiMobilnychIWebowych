package com.ziemniak.webserv;

import com.ziemniak.webserv.redis.RedisAccess;
import com.ziemniak.webserv.redis.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CRegister {

    @RequestMapping(value = "/auth/register", method = RequestMethod.POST)
    public RegisterResponse register(@RequestParam String nick, @RequestParam String password, @RequestParam String validatePassword){
        StringBuilder sb = new StringBuilder();
        boolean accepted = true;
        System.out.println(nick+" "+password);
        if(RedisAccess.exists(nick)){
            accepted = false;
            sb.append("User with this nickname already exists!");
        }if(!password.equals(validatePassword)){
            accepted = false;
            if(sb.length() != 0){
                sb.append('\n');
            }
            sb.append("Passwords don't match!");
        }
        String message;
        if(accepted){
            message = "Registered";
        }else{
            message = sb.toString();
        }if(accepted){
            User u  = new User();
            u.setPassword(password);
            u.setNick(nick);
            RedisAccess.save(u);
            System.out.println("Saving");
            System.out.println(RedisAccess.exists(u.getNick()));
        }
        return new RegisterResponse(nick, accepted, message);
    }

    @RequestMapping(value = "/auth/register", method = RequestMethod.GET)
    public RegisterResponse checkNick(@RequestParam String nick){
        boolean exists = RedisAccess.exists(nick);
        if(exists) {
            return new RegisterResponse(nick, false, "User with given nickname already exists");
        }else{
            return new RegisterResponse(nick, true,"Ok");
        }
    }
}
