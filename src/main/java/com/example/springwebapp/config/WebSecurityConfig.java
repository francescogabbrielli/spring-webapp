package com.example.springwebapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.sql.DataSource;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Autowired
    AuthenticationFailureHandler authenticationFailureHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/index", "/login", "/error", "/register", "/static/**", "/webjars/**").permitAll()
                .antMatchers("/user/**").hasAnyRole("USER", "ADMIN") //autorizza le pagine utente
                .antMatchers("/admin/**").hasRole("ADMIN") //autorizza le pagine admin
                .anyRequest().authenticated()
            .and().formLogin().loginPage("/login").failureHandler(authenticationFailureHandler)
            .and().logout().deleteCookies("JSESSIONID");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery(
                        "SELECT email AS principal, password AS credentials, TRUE FROM users WHERE email=?")
                .authoritiesByUsernameQuery(
                        "SELECT u.name AS principal, r.name AS role " +
                        "FROM roles r, user_roles ur, users u " +
                        "WHERE ur.role_id = r.id AND ur.user_id = u.id AND u.email=?"
                )
                .passwordEncoder(new BCryptPasswordEncoder());
    }
    

}
