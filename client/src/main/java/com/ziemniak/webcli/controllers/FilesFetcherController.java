package com.ziemniak.webcli.controllers;

import com.ziemniak.webcli.ClientApplication;
import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class FilesFetcherController {
    @GetMapping("/filesfetcher/{id}")
    public void fetchFile(@PathVariable int id, @CookieValue(defaultValue = "", value = "jwt") String jwt,
                          HttpServletResponse response) throws IOException {
        if ("".equals(jwt)) {
            response.sendRedirect("/login");
        } else {
            System.out.println("Redirecting to server for file ");
            response.sendRedirect(ClientApplication.URL_TO_SERVER + "/files/" + id);
        }
    }

}
