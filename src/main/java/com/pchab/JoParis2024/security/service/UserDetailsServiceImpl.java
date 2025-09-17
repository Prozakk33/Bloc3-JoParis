package com.pchab.JoParis2024.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pchab.JoParis2024.pojo.User;
import com.pchab.JoParis2024.repository.UserRepository;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("UserDetailsServiceImpl - loadUserByUsername() called with email: " + email);    
        try {
            User user = userRepository.findByEmail(email);
            System.out.println("UserDetailsServiceImpl - User found: " + userRepository.findByEmail(email));
            return UserDetailsImpl.build(user);
        } catch (Exception e) {
                System.err.println("UserDetailsServiceImpl - User not found with email: " + email);
                throw new UsernameNotFoundException("User Not Found with email : " + email);
        }
    }
}
