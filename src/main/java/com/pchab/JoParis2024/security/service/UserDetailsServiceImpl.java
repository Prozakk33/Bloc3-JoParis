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
        try {
            User user = userRepository.findUserByEmail(email);
            return UserDetailsImpl.build(user);
        } catch (Exception e) {
                throw new UsernameNotFoundException("User Not Found with email : " + email);
        }
    }
}
