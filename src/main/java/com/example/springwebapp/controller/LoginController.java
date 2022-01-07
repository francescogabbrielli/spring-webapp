package com.example.springwebapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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

    @Autowired private HttpSession session;

    @GetMapping("/login")
    public String login(Model model, String error, String logout) {

        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("bootstrapVersion", mavenConfig.getProperty("bootstrap.version"));

        if (error!=null) {
            error = ((Exception) session.getAttribute("loginError")).getLocalizedMessage();
            model.addAttribute("error", String.format("Errore di autenticazione [%s] ", error));
        }

        return "login";
    }

}