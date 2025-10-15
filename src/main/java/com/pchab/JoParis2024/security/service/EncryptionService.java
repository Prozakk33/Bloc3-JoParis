package com.pchab.JoParis2024.security.service;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EncryptionService {

    @Value("${AES_SECRET}") 
    private static String aesSecret;

    private static final String ALGORITHM = "AES";

    public String encrypt(String data) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(aesSecret.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // Déchiffrer une valeur
    public String decrypt(String encryptedData) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(aesSecret.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
        byte[] originalBytes = cipher.doFinal(decodedBytes);
        return new String(originalBytes);
    }
    
}
