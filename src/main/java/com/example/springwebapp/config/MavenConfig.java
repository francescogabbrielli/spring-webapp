package com.example.springwebapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 * Accesso alle properties dichiarate nel pom
 */
@Configuration
@PropertySource("classpath:pom.properties")
public class MavenConfig {

    @Autowired private Environment environment;

    public String getProperty(String name) {
        return environment.getProperty(name);
    }
    
}
