package com.example.springwebapp.service;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.example.springwebapp.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class UserService {
    
    @Autowired private DataSource dataSource;

    @Autowired private PasswordEncoder pwEncoder;

    @Autowired private RoleService roleService;

    public User getByEmail(String email) throws SQLException {

        User ret = null;

        try (
            Connection c = dataSource.getConnection();
            PreparedStatement s = c.prepareStatement("SELECT id,name,surname,birthday FROM users WHERE email=?");
        ) {
            
            s.setString(1, email);
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                ret = new User();
                ret.setEmail(email);
                ret.setId(rs.getLong(1));
                ret.setName(rs.getString(2));
                ret.setSurname(rs.getString(3));
                ret.setBirthday(rs.getDate(4));
            }
        }

        return ret;
    }

    public void createNew(User user) throws SQLException {

        try (
            Connection c = dataSource.getConnection();
            PreparedStatement s1 = c.prepareStatement(
                        "INSERT INTO users(name, surname, email, password, birthday) VALUES(?, ?, ?, ?, ?)",
                        PreparedStatement.RETURN_GENERATED_KEYS);
            PreparedStatement s2 = c.prepareStatement(
                        "INSERT INTO user_roles(user_id, role_id) VALUES(?, ?)");
    
        ) {
            s1.setString(1, user.getName());
            s1.setString(2, user.getSurname());
            s1.setString(3, user.getEmail());
            s1.setString(4, pwEncoder.encode(user.getPassword()));
            s1.setDate(5, new Date(user.getBirthday().getTime()));

            int affectedRows = s1.executeUpdate();
            if (affectedRows == 0)
                throw new SQLException("Creazione utente fallita: mancato inserimento nel DB");

            try (ResultSet generatedKeys = s1.getGeneratedKeys()) {
                if (!generatedKeys.next())
                    throw new SQLException("Creazione utente fallita: ID non restituito?");
                user.setId(generatedKeys.getLong(1));
                log.info("Utente creato con ID = {}", user.getId());

                //inserisce il collegamento al ruolo USER
                s2.setLong(1, user.getId());
                s2.setLong(2, roleService.getIdByName("USER"));
                s2.executeUpdate();
            }

        } catch (SQLException exc) {
            log.error("Errore inserimento utente", exc);
            throw exc;
        }

    }

}

