package com.pchab.JoParis2024.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pchab.JoParis2024.pojo.User;
import com.pchab.JoParis2024.service.UserService;

@RestController
@RequestMapping("api/user")

public class UserController {

    @Autowired
    private UserService userService;

    // Find a user by ID
    @GetMapping("/{id}")
    User findUserById(@PathVariable Long id) {
        return userService.findUserById(id);
    }

    // User Creation
    @PostMapping
    void createUser(@RequestBody User user) {
        userService.createUser(user);
    }
    
    // Find a user by email
    @GetMapping("/{email}")
    User findByEmail(@PathVariable String email) {
        try {
            User user = userService.findUserByEmail(email);
            return user;
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException("User Not Found with email : " + email);
        }
        
    }
}
