package com.example.springwebapp.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.example.springwebapp.model.User;
import com.example.springwebapp.utils.SessionUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

//TODO: trovare un metodo più semplice...
@Component
public class LoginFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {        

        if (request instanceof HttpServletRequest) {
            HttpServletRequest http = (HttpServletRequest) request;
            if (http.getRequestURI().contains("/login") && "POST".equalsIgnoreCase(http.getMethod())) {
                User user = SessionUtils.getSessionUser(http.getSession());
                if (user!=null) {
                    user.setEmail(request.getParameter("username"));
                    user.setRemember(request.getParameter("remember")!=null);
                }
            }
        }

        chain.doFilter(request, response);
    }
 
}