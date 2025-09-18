package com.pchab.JoParis2024.security.payload.request;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message="Email cannot be blank")
    @Email(message="Email should be valid")
    private String email;

    @NotBlank(message="Password cannot be blank")
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
