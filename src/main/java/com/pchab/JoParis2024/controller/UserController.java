package com.pchab.JoParis2024.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pchab.JoParis2024.pojo.User;
import com.pchab.JoParis2024.security.jwt.JwtUtils;
import com.pchab.JoParis2024.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="User", description="Endpoints for managing users")
@RestController
@RequestMapping("/user")

public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

  // Find a user by ID
    @Operation(summary = "Find user by ID", description = "Retrieve user details by their ID")
    @GetMapping("/id/{id}")
    User findUserById(@PathVariable Long id) {
        return userService.findUserById(id);
    }
 
    // Find a user by email
    @Operation(summary = "Find user by email", description = "Retrieve user details by their email address")
    @GetMapping("/email/{email}")
    User findByEmail(@PathVariable String email) {
        System.out.println("USERCONTROLLER - Searching for user with email: " + email);
        try {
            User user = userService.findUserByEmail(email);
            return user;
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException("User Not Found with email : " + email);
        }
    }

    @PostMapping("/userId")
    @Operation(summary = "Get user Id", description = "Retrieve user Id using JWT token")
    public ResponseEntity<?> getUserIdFromToken(@RequestHeader (value = "Authorization", required = true) String authorizationHeader) {
        System.out.println("USERID AUTH-CONTROLLER - Accessing userId with token: " + authorizationHeader);

        // Vérifier si l'en-tête Authorization est présent et commence par "Bearer "
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            System.err.println("USERID AUTH-CONTROLLER - Aucun token JWT trouvé dans l'en-tête Authorization.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Erreur: Accès non autorisé");
        }

        // Extraire le token JWT de l'en-tête Authorization
        String token = authorizationHeader.substring(7); // Supprime "Bearer " pour obtenir le token
        System.out.println("ACCOUNT AUTH-CONTROLLER - Token JWT reçu : " + token);
        
        if (!jwtUtils.validateJwtToken(token)) {
            System.err.println("ACCOUNT AUTH-CONTROLLER - Token JWT invalide.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Erreur: Token invalide");
        }

        // Si le token est valide, vous pouvez continuer à traiter la requête
        String email = jwtUtils.getEmailFromJwtToken(token); // Récupérer l'email depuis le token
        System.out.println("ACCOUNT AUTH-CONTROLLER - Email extrait du token : " + email);

        
        User user = userService.findUserByEmail(email);
        if(user != null) {
            System.out.println("ACCOUNT AUTH-CONTROLLER - Utilisateur trouvé pour l'email : " + email);
            return ResponseEntity.ok().body(user.getId());
        } else {
            System.err.println("ACCOUNT AUTH-CONTROLLER - Aucun utilisateur trouvé pour l'email : " + email);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Erreur: Accès non autorisé");
        }
    }
}
