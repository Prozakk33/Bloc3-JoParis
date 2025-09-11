package com.pchab.JoParis2024.service;

import pojo.User;

public interface UserService {

    // Find Event
    User findUserById(Long id);

    // Update Event
    void updateUserById(User user, Long id);

    // Delete Event
    void deleteUserById(Long id);

    //Create Event
    void createUser(User user);

}
