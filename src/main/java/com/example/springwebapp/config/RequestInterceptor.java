package com.example.springwebapp.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.springwebapp.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class RequestInterceptor implements HandlerInterceptor {

    @Autowired private MavenConfig mavenConfig;

    @Autowired private User user;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) 
      throws Exception {

        if(modelAndView!=null) {
            modelAndView.getModel().put("user", user);
            modelAndView.getModel().put("bootstrapVersion", mavenConfig.getProperty("bootstrap.version"));
        }

    }

}