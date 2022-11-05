package com.example.mysecuritybreachtest;

import java.util.List;

public interface UserService {

    List<User> listAll();
    User findByUsername(String username);
    boolean checkUsernameUnique(User user);
    User registerUser(boolean isSecure, User user);
    boolean checkUsernameExists(User user);
    boolean checkPassword(boolean isSecure, User user);

}
