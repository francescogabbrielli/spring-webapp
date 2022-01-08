package com.example.springwebapp.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.springwebapp.model.User;
import com.example.springwebapp.service.UserService;
import com.example.springwebapp.utils.SessionUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class WebAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        try {

            UserDetails details = (UserDetails) authentication.getPrincipal();
            User dbUser = userService.getByEmail(details.getUsername());
            User sessionUser = SessionUtils.getSessionUser(request.getSession());
            dbUser.setRemember(sessionUser.isRemember());

            SessionUtils.setSessionUser(request.getSession(), dbUser);

        } catch(Exception exc) {
            exc.printStackTrace();
        }
                
        super.onAuthenticationSuccess(request, response, authentication);
    }

}