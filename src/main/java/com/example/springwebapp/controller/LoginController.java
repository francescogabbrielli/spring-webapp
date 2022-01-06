package com.example.springwebapp.controller;

import com.example.springwebapp.config.MavenConfig;
import com.example.springwebapp.model.User;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class LoginController {

    @Autowired private MavenConfig mavenConfig;

    @GetMapping("/login")
    public String login(Model model, String error, String logout) {

        log.info("LOGIN: " + mavenConfig.getProperty("bootstrap.version"));

        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("bootstrapVersion", mavenConfig.getProperty("bootstrap.version"));

        if (error!=null)
            model.addAttribute("error", "Il nome e la password non sono validi: " + error);

        return "login";
    }

}