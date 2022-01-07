package com.example.springwebapp.model;

import lombok.Data;

import org.springframework.data.annotation.Transient;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class User {

    private long id;

    private String name;

    private String surname;

    private String email;

    private String password;

    @Transient
    private boolean remember;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date birthday;
}
