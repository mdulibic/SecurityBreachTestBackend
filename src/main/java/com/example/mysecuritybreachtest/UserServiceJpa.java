package com.example.mysecuritybreachtest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceJpa implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> listAll() {
        return userRepo.findAll();
    }

    @Override
    public User findByUsername(String username) {
        return userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("No user with username: " + username));
    }

    @Override
    public boolean checkUsernameUnique(User user) {
        return userRepo.countByUsername(user.getUsername()) == 0;
    }

    @Override
    public User registerUser(boolean isSecure, User user) {
        if(isSecure) user.setPassword(passwordEncoder.encode(user.getPassword()));
        else user.setPassword(user.getPassword());
        return userRepo.save(user);
    }

    @Override
    public boolean checkUsernameExists(User user) {
        if (userRepo.countByUsername(user.getUsername()) == 0) {
            return false;
        }
        return true;
    }

    @Override
    public boolean checkPassword(boolean isSecure, User user) {
        if(isSecure) return passwordEncoder.matches(user.getPassword(), userRepo.findByUsername(user.getUsername()).orElseThrow(() -> new UsernameNotFoundException("No user with username: " + user.getUsername())).getPassword());
        else return user.getPassword().equals(userRepo.findByUsername(user.getUsername()).orElseThrow(() -> new UsernameNotFoundException("No user with username: " + user.getUsername())).getPassword());
    }
}
