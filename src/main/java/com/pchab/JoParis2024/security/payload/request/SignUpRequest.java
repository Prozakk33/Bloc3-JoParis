package com.pchab.JoParis2024.security.payload.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpRequest {
                
    @NotBlank(message="Email cannot be blank")
    @Email(message="Email should be valid")
    private String email;

    @NotBlank(message="First name cannot be blank")
    private String firstName;

    @NotBlank(message="Last name cannot be blank")  
    private String lastName;

    @NotBlank(message="Password cannot be blank")
    @Size(min = 8, max = 40)
    @Pattern(regexp = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%&]).{10,}", message = "Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule, un chiffre et un caractère spécial.")
    private String password;


    public String getUserEmail() {
        System.out.println("SignUpRequest Email: " + email);
        return email;
    }

    public String getUserPassword() {
        System.out.println("SignUpRequest Password: " + password);
        return password;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }        
}
