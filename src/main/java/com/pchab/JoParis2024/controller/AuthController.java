package com.pchab.JoParis2024.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pchab.JoParis2024.pojo.User;
import com.pchab.JoParis2024.repository.UserRepository;
import com.pchab.JoParis2024.security.jwt.JwtUtils;
import com.pchab.JoParis2024.security.payload.request.LoginRequest;
import com.pchab.JoParis2024.security.payload.request.SignUpRequest;
import com.pchab.JoParis2024.security.payload.response.JwtResponse;
import com.pchab.JoParis2024.security.service.UserDetailsImpl;
import com.pchab.JoParis2024.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name="Authentication", description="Endpoints for user authentication and registration")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private UserRepository  userRepository;

    @Autowired
    private UserController userController;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserService userService;

    // Login authentification
    @Operation(summary = "User login", description = "Authenticate user and return JWT token")
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

    //@Operation(summary = "Get user account", description = "Retrieve user account details using JWT token")
    @GetMapping("/account")
    public String account(@RequestHeader (value = "Authorization", required = true) String authorizationHeader, Model model) {
        System.out.println("ACCOUNT AUTH-CONTROLLER - Accessing account with token: " + authorizationHeader);

        // Vérifier si l'en-tête Authorization est présent et commence par "Bearer "
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            System.err.println("ACCOUNT AUTH-CONTROLLER - Aucun token JWT trouvé dans l'en-tête Authorization.");
            return "redirect:/user/signin"; // Redirige vers la page de connexion
        }

        // Extraire le token JWT de l'en-tête Authorization
        String token = authorizationHeader.substring(7); // Supprime "Bearer " pour obtenir le token
        System.out.println("ACCOUNT AUTH-CONTROLLER - Token JWT reçu : " + token);
        
        if (!jwtUtils.validateJwtToken(token)) {
            System.err.println("ACCOUNT AUTH-CONTROLLER - Token JWT invalide.");
            return "redirect:/signin"; // Redirige si le token est invalide
        }

        // Si le token est valide, vous pouvez continuer à traiter la requête
        String email = jwtUtils.getEmailFromJwtToken(token); // Récupérer l'email depuis le token
        System.out.println("ACCOUNT AUTH-CONTROLLER - Email extrait du token : " + email);

        System.out.println("ACCOUNT AUTH-CONTROLLER - Utilisateur trouvé pour l'email : " + email);
        User user = userService.findUserByEmail(email);
        if(user != null) {
            model.addAttribute("user", user);
            return "userAccount";
        } else {
            System.err.println("ACCOUNT AUTH-CONTROLLER - Aucun utilisateur trouvé pour l'email : " + email);
            return "redirect:/signin"; // Redirige si aucun utilisateur n'est trouvé
        }
    }

    // User Registration
    //@Operation(summary = "User registration", description = "Register a new user account")
    @PostMapping("/signup")
    public String signUp(@Valid @ModelAttribute("signUpRequest") SignUpRequest signUpRequest, BindingResult result, Model model) {
        if(result.hasErrors()) {
            return "signup"; // Return to the signup page with errors
        }

        if (userRepository.findByEmail(signUpRequest.getUserEmail()) != null) {
            model.addAttribute("emailError", "Error: Email is already in use !");
            return "signup"; // Return to the signin page with email error
        }

        // Create new user's account
        User user = new User(
            signUpRequest.getFirstName(),
            signUpRequest.getLastName(),
            signUpRequest.getUserEmail(),
            passwordEncoder.encode(signUpRequest.getUserPassword()), UUID.randomUUID().toString()      
        );
        userService.createUser(user);

        return "redirect:/user/signin?success";
    }  

}
