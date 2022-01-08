package com.example.springwebapp.controller;

import com.example.springwebapp.model.User;
import com.example.springwebapp.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {

    @Autowired UserService userService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        return "register";
    }

    @PostMapping("/register")
    public String submitRegisterForm(Model model, @ModelAttribute("user") User user, BindingResult result) {

        // validazione lato server
        if (result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors());
            return "register";
        }

        try {
            userService.createNew(user);
        } catch(Exception exc) {
            model.addAttribute("error", exc.toString());
            return "register";
        }

        return "register_success";

    }

}
