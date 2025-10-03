package com.pchab.JoParis2024.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.*;

@Component
public class EnvironmentLogger {

    @Value("${DB_USER:non défini}")
    private String dbUser;

    @Value("${DB_PASSWORD:non défini}")
    private String dbPassword;

    @Value("${JWT_SECRET:non défini}")
    private String jwtSecret;

    @PostConstruct
    public void logEnvironmentVariables() {
        System.out.println("DB_USER: " + dbUser);
        System.out.println("DB_PASSWORD: " + dbPassword);
        System.out.println("JWT_SECRET: " + jwtSecret);
    }
} 
