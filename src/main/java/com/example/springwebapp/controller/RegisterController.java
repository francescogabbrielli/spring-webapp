package com.example.springwebapp.controller;

import com.example.springwebapp.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
@Controller
public class RegisterController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register")
    public String submitRegisterForm(Model model, @ModelAttribute("user") User user, BindingResult result) {

        // validazione lato server
        if (result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors());
            return "register";
        }

        // inserimento nel DB
        try (
            Connection c = dataSource.getConnection();
            PreparedStatement s = c.prepareStatement(
                        "INSERT INTO users(name, surname, email, password, birthday) VALUES(?, ?, ?, ?, ?)");
        ) {

            int affectedRows = s.executeUpdate();
            if (affectedRows == 0)
                throw new SQLException("Creazione utente fallita: mancato inserimento nel DB");

            try (ResultSet generatedKeys = s.getGeneratedKeys()) {
                if (!generatedKeys.next())
                    throw new SQLException("Creazione utente fallita: ID non restituito?");
                user.setId(generatedKeys.getLong(1));
                log.info("Utente creato con ID = {}", user.getId());
            }

        } catch (SQLException exc) {
            log.error("Errore inserimento utente", exc);
            model.addAttribute("error", exc);
            return "register";
        }

        return "register_success";

    }

}
