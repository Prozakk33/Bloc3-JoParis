package com.pchab.JoParis2024.service;

import com.pchab.JoParis2024.pojo.User;

public interface UserService {

    // Find User
    User findUserById(Long id);

    // Update User
    void updateUserById(User user, Long id);

    // Delete User
    void deleteUserById(Long id);

    //Create User
    void createUser(User user);

}
