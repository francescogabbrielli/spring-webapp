package com.example.springwebapp.controller;

import javax.servlet.http.HttpSession;

import com.example.springwebapp.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @Autowired private HttpSession session;

    @GetMapping("/login")
    public String login(Model model, String error, String logout) {

        if (error!=null) {
            error = ((Exception) session.getAttribute("loginError")).getLocalizedMessage();
            model.addAttribute("error", String.format("Errore di autenticazione [%s] ", error));
        }

        return "login";
    }

}