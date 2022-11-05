package com.example.mysecuritybreachtest.security;

import com.example.mysecuritybreachtest.User;
import com.example.mysecuritybreachtest.UserService;
import com.example.mysecuritybreachtest.UserServiceJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class SecurityBreachUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new SecurityBreachTestUserDetails(user);
    }


}
