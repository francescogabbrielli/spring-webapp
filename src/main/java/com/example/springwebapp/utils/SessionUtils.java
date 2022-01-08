package com.example.springwebapp.utils;

import javax.servlet.http.HttpSession;

import com.example.springwebapp.model.User;

public class SessionUtils {
    
    private final static String SESSION_USER_KEY = "scopedTarget.sessionUser";


    public final static User getSessionUser(HttpSession session) {
        return session!=null ? (User) session.getAttribute(SESSION_USER_KEY) : null;
    }

    public final static void setSessionUser(HttpSession session, User user) {
        session.setAttribute(SESSION_USER_KEY, user);
    }

}
