package com.example.springwebapp.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleService {

    @Autowired private DataSource dataSource;

    public int getIdByName(String roleName) throws SQLException {

        try (
            Connection c = dataSource.getConnection();
            PreparedStatement s = c.prepareStatement("SELECT id FROM roles WHERE name=?");
        ) {

            s.setString(1, roleName);
            ResultSet rs = s.executeQuery();
            if(rs.next())
                return rs.getInt(1);
            
        }

        throw new SQLException(String.format("Il ruolo %s non esiste", roleName));

    }
    
}
