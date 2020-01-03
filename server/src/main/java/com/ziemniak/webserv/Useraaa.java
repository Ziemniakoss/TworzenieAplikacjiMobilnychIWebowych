package com.ziemniak.webserv;

import java.io.Serializable;
import java.util.ArrayList;


public class Useraaa implements Serializable {
    private String username;
    private String password;

    public Useraaa(String foo, String foo1, ArrayList<Object> objects) {
    }

    public Useraaa() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
