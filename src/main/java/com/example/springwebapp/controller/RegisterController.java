package com.example.springwebapp.controller;

import com.example.springwebapp.config.MavenConfig;
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
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
@Controller
public class RegisterController {

    @Autowired private DataSource dataSource;

    @Autowired private MavenConfig mavenConfig;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("bootstrapVersion", mavenConfig.getProperty("bootstrap.version"));
        return "register";
    }

    @PostMapping("/register")
    public String submitRegisterForm(Model model, @ModelAttribute("user") User user, BindingResult result) {

        model.addAttribute("bootstrapVersion", mavenConfig.getProperty("bootstrap.version"));

        // validazione lato server
        if (result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors());
            return "register";
        }

        // inserimento nel DB
        try (
            Connection c = dataSource.getConnection();
            PreparedStatement s = c.prepareStatement(
                        "INSERT INTO users(name, surname, email, password, birthday) VALUES(?, ?, ?, ?, ?)",
                        PreparedStatement.RETURN_GENERATED_KEYS);
        ) {
            s.setString(1, user.getName());
            s.setString(2, user.getSurname());
            s.setString(3, user.getEmail());
            s.setString(4, user.getPassword());
            s.setDate(5, new Date(user.getBirthday().getTime()));

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
            model.addAttribute("error", exc.toString());
            return "register";
        }

        model.addAttribute("user", user);
        return "register_success";

    }

}
