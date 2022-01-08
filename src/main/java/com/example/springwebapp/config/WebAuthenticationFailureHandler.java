package com.example.springwebapp.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class WebAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {

        // salva l'errore in sessione: (TODO: migliorare per esempio passandolo in post?)
        request.getSession().removeAttribute("loginError");
        if (exception != null) {
            exception.printStackTrace();
            setDefaultFailureUrl("/login?error");
            request.getSession().setAttribute("loginError", exception);
        }

        super.onAuthenticationFailure(request, response, exception);

    }

}