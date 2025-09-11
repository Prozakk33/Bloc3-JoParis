package com.pchab.JoParis2024.service;

import com.pchab.JoParis2024.pojo.User;

public interface UserService {

    // Find User
    User findUserById(Long user_id);

    // Update User
    void updateUserById(User user, Long user_id);

    // Delete User
    void deleteUserById(Long user_id);

    //Create User
    void createUser(User user);

}
