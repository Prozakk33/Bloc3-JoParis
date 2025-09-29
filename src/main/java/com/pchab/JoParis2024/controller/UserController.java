package com.pchab.JoParis2024.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pchab.JoParis2024.pojo.User;
import com.pchab.JoParis2024.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="User", description="Endpoints for managing users")
@RestController
@RequestMapping("/user")

public class UserController {

    @Autowired
    private UserService userService;

  // Find a user by ID
    @Operation(summary = "Find user by ID", description = "Retrieve user details by their ID")
    @GetMapping("/id/{id}")
    User findUserById(@PathVariable Long id) {
        return userService.findUserById(id);
    }
 
    // Find a user by email
    @Operation(summary = "Find user by email", description = "Retrieve user details by their email address")
    @GetMapping("/email/{email}")
    User findByEmail(@PathVariable String email) {
        System.out.println("USERCONTROLLER - Searching for user with email: " + email);
        try {
            User user = userService.findUserByEmail(email);
            return user;
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException("User Not Found with email : " + email);
        }
    }
}
