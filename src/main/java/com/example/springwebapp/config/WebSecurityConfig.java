package com.example.springwebapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.context.annotation.SessionScope;

import javax.sql.DataSource;

import com.example.springwebapp.model.User;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Autowired
    AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    LoginFilter loginFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(loginFilter, UsernamePasswordAuthenticationFilter.class)//salva remember in sessione
            //cookie che salva il flag "ricorda" (in base alla checkbox remember nel login)
            .rememberMe()
                .key("rem-me-key")
                .rememberMeParameter("remember")
                .rememberMeCookieName("remember-me")
                .tokenValiditySeconds(60 * 60 * 24) 
                .useSecureCookie(true)
            //autorizzazioni
            .and()
                .authorizeRequests()
                //pagine pubbliche
                .antMatchers("/", "/index", "/login", "/error", "/register", "/static/**", "/webjars/**").permitAll()
                //autorizza le pagine utente
                .antMatchers("/user/**").hasAnyRole("USER", "ADMIN") 
                //autorizza le pagine admin
                .antMatchers("/admin/**").hasRole("ADMIN")
                //imposta le autorizzazioni
                .anyRequest().authenticated()
            //pagina di login (con salvataggio dell'errore in sessione)
            .and().formLogin().loginPage("/login")
                .failureHandler(authenticationFailureHandler)
                .successHandler(authenticationSuccessHandler)
            //logout
            .and().logout().deleteCookies("JSESSIONID");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
                .passwordEncoder(passwordEncoder())
                .usersByUsernameQuery(
                        "SELECT email AS principal, password AS credentials, TRUE FROM users WHERE email=?")
                .authoritiesByUsernameQuery(
                        "SELECT u.name AS principal, r.name AS role " +
                        "FROM roles r, user_roles ur, users u " +
                        "WHERE ur.role_id = r.id AND ur.user_id = u.id AND u.email=?"
                )
                .rolePrefix("ROLE_");
    }
 
    //Esporta il password encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    //Utente di sessione
    @Bean @SessionScope
    public User sessionUser() {
        return new User();
    }
    
}