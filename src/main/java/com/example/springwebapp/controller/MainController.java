package com.example.springwebapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String index() { return "user/dashboard"; }

    @GetMapping("/error")
    public String error() { return "error"; }

}
