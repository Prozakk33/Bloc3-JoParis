package com.pchab.JoParis2024;

import com.pchab.JoParis2024.security.jwt.JwtUtils;
import com.pchab.JoParis2024.security.payload.request.LoginRequest;
import com.pchab.JoParis2024.security.payload.request.SignUpRequest;
import com.pchab.JoParis2024.security.service.UserDetailsImpl;
import com.pchab.JoParis2024.service.UserService;
import com.pchab.JoParis2024.controller.AuthController;
import com.pchab.JoParis2024.pojo.User;
import com.pchab.JoParis2024.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    @Test
    public void testSignIn_Success() throws Exception {
        // Préparer les données
        LoginRequest loginRequest = new LoginRequest();
        String email = "testloginsuccess@example.com";
        String password = "P@ssword123";
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        Authentication authentication = Mockito.mock(Authentication.class);
        UserDetailsImpl userDetails = new UserDetailsImpl(email, password, List.of());

        // Configurer les mocks
        when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("mock-jwt-token");
        when(authentication.getPrincipal()).thenReturn(userDetails);

        // JSON de la requête
        String loginJson = """
            {
              "email": "testloginsuccess@example.com",
              "password": "P@ssword123"
            }
        """;

        // Exécuter la requête et vérifier la réponse
        mockMvc.perform(post("/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock-jwt-token"));
    }

    @Test
    public void testSignIn_Failure() throws Exception {
        // Préparer les données
        String loginJson = """
            {
              "email": "wronguser@example.com",
              "password": "wrongpassword"
            }
        """;

        // Configurer les mocks
        when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Authentication failed"));

        // Exécuter la requête et vérifier la réponse
        mockMvc.perform(post("/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Erreur: Email ou mot de passe invalide"));
    }

    @Test
    public void testSignUp_Success() throws Exception {
        // JSON de la requête
        String signUpJson = """
            {
              "firstName": "John",
              "lastName": "Doe",
              "email": "signUpSuccess@example.com",
              "password": "P@ssword123"
            }
        """;
        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        when(userRepository.findByEmail("signUpSuccess@example.com")).thenReturn(null);

        // Exécuter la requête et vérifier la réponse
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(signUpJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Inscription réussie !"));
    }

    @Test
    public void testSignUp_EmailAlreadyExists() throws Exception {
        // Préparer les données
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("existingUser@example.com");
        signUpRequest.setPassword("P@assword123");
        signUpRequest.setFirstName("John");
        signUpRequest.setLastName("Doe");

        // Configurer les mocks
        when(userRepository.findByEmail("existingUser@example.com")).thenReturn(new User());

        // JSON de la requête
        String signUpJson = """
            {
              "firstName": "John",
              "lastName": "Doe",
              "email": "existingUser@example.com",
              "password": "P@ssword123456"
            }
        """;

        // Exécuter la requête et vérifier la réponse
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(signUpJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Erreur: Email déjà existant !"));
    }
}