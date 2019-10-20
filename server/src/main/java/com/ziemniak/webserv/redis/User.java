package com.ziemniak.webserv.redis;

import java.io.Serializable;


public class User implements Serializable {
    private String nick;
    private String password;

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
