package com.pchab.JoParis2024.service;

import com.pchab.JoParis2024.pojo.User;

public interface UserService {

    // Find User
    User findUserById(Long id);

    //Create User
    void createUser(User user);

    User findUserByEmail(String email);

}
