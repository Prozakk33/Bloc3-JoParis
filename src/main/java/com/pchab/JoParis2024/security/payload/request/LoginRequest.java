package com.pchab.JoParis2024.security.payload.request;
import lombok.Setter;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class LoginRequest {
    
    @NotBlank
    private String username;

    @NotBlank
    private String password;
    public String getUsername() {
        return username;
    }
}       
