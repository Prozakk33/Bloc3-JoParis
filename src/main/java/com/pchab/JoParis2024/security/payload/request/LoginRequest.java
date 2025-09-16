package com.pchab.JoParis2024.security.payload.request;
import lombok.Setter;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class LoginRequest {
    
    @NotBlank
    private String email;

    @NotBlank
    private String password;
    
    public String getEmail() {
        System.out.println("LoginRequest Email: " + email);
        return email;
    }

    public String getPassword() {
        System.out.println("LoginRequest Password: " + password);
        return password;
    }
}       
