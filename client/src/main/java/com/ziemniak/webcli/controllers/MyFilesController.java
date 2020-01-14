package com.ziemniak.webcli.controllers;

import com.ziemniak.webcli.ClientApplication;
import com.ziemniak.webcli.File;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Controller
public class MyFilesController {
    @GetMapping("/myfiles")
    public String myFiles(@CookieValue(value = "jwt", defaultValue = "") String jwt, Model model) {
        if ("".equals(jwt)) {
            return "redirect:/login";
        }
        //Pobieramy liste publikacji

        File[] files = fetchFileList(jwt);
        model.addAttribute("fileList", files);
        return "myfiles";
    }

    private File[] fetchFileList(String jwt) {
        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwt);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity<>("", headers);
        String url = ClientApplication.URL_TO_SERVER + "/files/getall";
        ResponseEntity<File[]> response = rt.exchange(url, HttpMethod.GET, entity, com.ziemniak.webcli.File[].class, new HashMap<>());
        return response.getBody();
    }

}
