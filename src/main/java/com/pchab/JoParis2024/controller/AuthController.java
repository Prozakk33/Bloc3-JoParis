package com.pchab.JoParis2024.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pchab.JoParis2024.pojo.User;
import com.pchab.JoParis2024.repository.UserRepository;
import com.pchab.JoParis2024.security.jwt.JwtUtils;
import com.pchab.JoParis2024.security.payload.request.LoginRequest;
import com.pchab.JoParis2024.security.payload.request.SignUpRequest;
import com.pchab.JoParis2024.security.payload.response.JwtResponse;
import com.pchab.JoParis2024.security.service.UserDetailsImpl;
import com.pchab.JoParis2024.service.UserService;

import jakarta.validation.Valid;



@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private UserRepository  userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserService userService;

    // Login authentification
    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@Valid @RequestBody LoginRequest loginRequest) {
        try {
        System.err.println("AUTH-CONTROLLER - Login attempt for email: " + loginRequest.getEmail()  + " with password: " + loginRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        System.out.println("AUTH-CONTROLLER - Authentication successful for email: " + loginRequest.getEmail());
        
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
     
        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername()));
        } catch (Exception e) {
            System.err.println("AUTH-CONTROLLER - Authentication failed for email: " + loginRequest.getEmail() + " - " + e.getMessage());
            return ResponseEntity
                    .badRequest()
                    .body("Error: Invalid email or password");
        }
    }

    @GetMapping("/account")
    public String account(Model model) {
        return "userAccount";
    }



    // User Registration
    @PostMapping("/signup")
    public String signUp(@Valid @ModelAttribute("signUpRequest") SignUpRequest signUpRequest, BindingResult result, Model model) {
        if(result.hasErrors()) {
            return "signup"; // Return to the signup page with errors
        }

        if (userRepository.findByEmail(signUpRequest.getUserEmail()) != null) {
            model.addAttribute("emailError", "Error: Email is already in use !");
            return "signup"; // Return to the signup page with email error
        }

        // Create new user's account
        User user = new User(
            signUpRequest.getFirstName(),
            signUpRequest.getLastName(),
            signUpRequest.getUserEmail(),
            passwordEncoder.encode(signUpRequest.getUserPassword()), UUID.randomUUID().toString()      
        );
        userService.createUser(user);

        return "redirect:/";
    }  

}
