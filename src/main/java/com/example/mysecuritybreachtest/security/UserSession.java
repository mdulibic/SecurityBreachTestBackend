package com.example.mysecuritybreachtest.security;

import com.example.mysecuritybreachtest.User;
import com.example.mysecuritybreachtest.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserSession {

    @Autowired
    private UserService userService;

    public User getUser() {
        return userService.findByUsername(getUsername());
    }

    public String getUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ((SecurityBreachTestUserDetails) auth.getPrincipal()).getUsername();
    }

}
