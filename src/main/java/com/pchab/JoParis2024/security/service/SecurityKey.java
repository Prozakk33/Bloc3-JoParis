package com.pchab.JoParis2024.security.service;

import java.security.SecureRandom;
import java.util.Base64;

import org.springframework.stereotype.Component;

@Component
public class SecurityKey {

    // Define Key Length
    private static final int KEY_LENGTH = 24; 

    // Key generation method
    public static String generateSecureKey() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[KEY_LENGTH];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}
    

