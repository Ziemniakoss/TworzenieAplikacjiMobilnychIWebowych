package com.ziemniak.webcli.controllers;

import com.ziemniak.webcli.ClientApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LogoutController {
    private final Logger log = LoggerFactory.getLogger(LogoutController.class);

    @PostMapping(value = "/logout")
    public String logout(@CookieValue(value = "jwt", defaultValue = "") String jwt, HttpServletResponse httpServletResponse) {
        if ("".equals(jwt)) {
            return "redirect:/";
        }
        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwt);
        headers.set("Content-Type", "application/json");
        HttpEntity entity = new HttpEntity(null, headers);
        String url = ClientApplication.URL_TO_SERVER + "/auth/logout";
        rt.postForEntity(url, entity, Void.class);
        log.info("User with jwt " + jwt + "logged out");
        httpServletResponse.addCookie(new Cookie("jwt", ""));
        return "redirect:/";
    }
}
