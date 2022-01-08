package com.example.springwebapp.utils;

import javax.servlet.http.HttpSession;

import com.example.springwebapp.model.User;

public class SessionUtils {
    
    private final static String SESSION_PREFIX = "scopedTarget.";
    private final static String SESSION_USER_KEY = "sessionUser";

    public final static <T> T getSessionBean(HttpSession session, Class<T> beanClass) {
        String n = beanClass.getSimpleName();
        if (n.length()>1)
            n = n.substring(0,1).toLowerCase() + n.substring(1);
        return (T) getSessionBean(session, n);
    }

    public final static Object getSessionBean(HttpSession session, String name) {
        return session!=null ? session.getAttribute(SESSION_PREFIX + name) : null;
    }

    public final static void setSessionBean(HttpSession session, String name, Object obj) {
        session.setAttribute(SESSION_PREFIX + name, obj);
    }

    public final static User getSessionUser(HttpSession session) {
        return (User) getSessionBean(session, SESSION_USER_KEY);
    }

    public final static void setSessionUser(HttpSession session, User user) {
        setSessionBean(session, SESSION_USER_KEY, user);
    }

}
