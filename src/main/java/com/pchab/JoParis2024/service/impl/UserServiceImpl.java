package com.pchab.JoParis2024.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pchab.JoParis2024.pojo.User;
import com.pchab.JoParis2024.repository.UserRepository;
import com.pchab.JoParis2024.security.jwt.JwtUtils;
import com.pchab.JoParis2024.security.service.EncryptionService;
import com.pchab.JoParis2024.security.service.SecurityKey;
import com.pchab.JoParis2024.service.UserService;

@Service

public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EncryptionService encryptionService;

    @Autowired 
    private JwtUtils jwtUtils;

    @Autowired
    private SecurityKey securityKey;

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void createUser(User user) {
        System.out.println("USERSERVICE-IMPL - Creating user: " + user.getEmail());
        String userKey = securityKey.generateSecureKey();

        String encryptedUserKey = null;
        try {
            encryptedUserKey = encryptionService.encrypt(userKey);
        } catch (Exception e) {
            System.err.println("USERSERVICE-IMPL - Error encrypting user key: " + e.getMessage());
            throw new RuntimeException("Error encrypting user key", e);
        }
        user.setUserKey(encryptedUserKey);
        userRepository.save(user);
    }

    @Override
    public User findUserByEmail(String email) {
        System.out.println("USERSERVICE-IMPL - Searching for user with email: " + email);
        try {
            User user = userRepository.findByEmail(email);
            System.out.println("USERSERVICE-IMPL - User found: " + user);
            return user;
        } catch (Exception e) {
            throw new UsernameNotFoundException("User Not Found with email : " + email);
        }
    }
    
}
