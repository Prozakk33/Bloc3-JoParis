package com.pchab.JoParis2024;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.pchab.JoParis2024.controller.AuthController;
import com.pchab.JoParis2024.pojo.User;
import com.pchab.JoParis2024.repository.UserRepository;
import com.pchab.JoParis2024.security.jwt.JwtUtils;
import com.pchab.JoParis2024.security.payload.request.LoginRequest;
import com.pchab.JoParis2024.security.payload.request.SignUpRequest;
import com.pchab.JoParis2024.security.service.UserDetailsImpl;
import com.pchab.JoParis2024.service.UserService;



@ExtendWith(MockitoExtension.class)
public class AuthControllerUnitTest {

    private MockMvc mockMvc;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    public void testAccount_Success() {
        String jwtToken = "mock-jwt-token";
        String authorizationHeader = "Bearer " + jwtToken;

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setFirstName("FirstName");
        mockUser.setLastName("LastName");
        mockUser.setEmail("testuser@example.com");
        mockUser.setPassword("encodedpassword");

        when(jwtUtils.validateJwtToken(jwtToken)).thenReturn(true);
        when(jwtUtils.getEmailFromJwtToken(jwtToken)).thenReturn("testuser@example.com");
        when(userService.findUserByEmail("testuser@example.com")).thenReturn(mockUser);

        ResponseEntity<?> response = authController.account(authorizationHeader);

        //System.out.println("Response: " + response.toString());
        assert(response.getStatusCode().is2xxSuccessful());
        assert(response.getBody() != null);
        // Simulation du comportement du service
        //when(authController.account(authorizationHeader)).thenReturn(ResponseEntity.ok(new JwtResponse(jwtToken)));


    }

    @Test
    public void testAccount_BadToken() {
        String jwtToken = "invalid-jwt-token";
        String authorizationHeader = "Bearer " + jwtToken;

        when(jwtUtils.validateJwtToken(jwtToken)).thenReturn(false);
        ResponseEntity<?> response = authController.account(authorizationHeader);
        assert(response.getStatusCode().is4xxClientError());
    }

    @Test
    public void testAccount_NoUser() {
        String jwtToken = "mock-jwt-token";
        String authorizationHeader = "Bearer " + jwtToken;
        when(jwtUtils.validateJwtToken(jwtToken)).thenReturn(true);
        when(jwtUtils.getEmailFromJwtToken(jwtToken)).thenReturn("testuser@example.com");
        when(userService.findUserByEmail("testuser@example.com")).thenReturn(null);

        ResponseEntity<?> response = authController.account(authorizationHeader);
        assert(response.getStatusCode().is4xxClientError());
    }

    @Test
    public void testAccount_NoHeader() {
        ResponseEntity<?> response = authController.account(null);
        assert(response.getStatusCode().is4xxClientError());
    }
// ----------------------------------------------------------------

    @Test
    public void testSignIn_Success() throws Exception {
        String email = "testuser@example.com";
        String password = "P@ssword123456";
        String jwtToken = "mock-jwt-token";
        
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        UserDetailsImpl userDetails = new UserDetailsImpl(email, password, new ArrayList<>());

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        // Simulation des appels de service

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenReturn(authentication);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn(jwtToken);

        // Corps de la requête JSON
        String requestBody = """
            {
                "email": "test@example.com",
                "password": "P@ssword123456"
            }
        """;

        // Exécution de la requête POST
        mockMvc.perform(post("/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)) 
                .andExpect(status().isOk()) 
                .andExpect(jsonPath("$.token").value(jwtToken)); 
                //.andDo(print()); 
    }

    @Test
    public void testSignIn_Failure() throws Exception {
        String email = "testuser@example.com";
        String password = "invalidpassword";

        // Corps de la requête JSON
        String requestBody = """
            {
                "email": "%s",
                "password": "%s"
            }
        """.formatted(email, password);

        // Exécution de la requête POST
        mockMvc.perform(post("/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)) 
                .andExpect(status().isBadRequest()); 
                //.andDo(print()); 
    }

// ---------------------------------------------------------------- 

    @Test
    public void testSignUp_Success() throws Exception {
        // Implement your test logic here
        String email = "testuser@example.com";
        String password = "P@ssword123456";
        String firstName = "John";
        String lastName = "Doe";

        User mockUser = new User();
        mockUser.setEmail(email);
        mockUser.setPassword("encodedPassword");
        mockUser.setFirstName(firstName);
        mockUser.setLastName(lastName);
        

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail(email);
        signUpRequest.setPassword(password);
        signUpRequest.setFirstName(firstName);
        signUpRequest.setLastName(lastName);

        when(userRepository.findByEmail(email)).thenReturn(null);
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
        //when(userService.createUser(mockUser)).thenReturn(mockUser);
        // Corps de la requête JSON
        
        String requestBody = """
            {
                "email": "%s",
                "password": "%s",
                "firstName": "%s",
                "lastName": "%s"
            }
        """.formatted(email, password, firstName, lastName);


        // Exécution de la requête POST
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)) // Ajout du corps de la requête
                .andExpect(status().isOk()); // Vérifie que le statut HTTP est 201
                //.andDo(print()); // Affiche la requête et la réponse dans la console

    }

    @Test
    public void testSignUp_Failure() throws Exception {
        // Implement your test logic here
        String email = "testuser@example.com";
        String password = "P@ssword123456";
        String firstName = "John";
        String lastName = "Doe";
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail(email);
        signUpRequest.setPassword(password);
        signUpRequest.setFirstName(firstName);
        signUpRequest.setLastName(lastName);

        when(userRepository.findByEmail(email)).thenReturn(new User());

        String requestBody = """
            {
                "email": "%s",
                "password": "%s",
                "firstName": "%s",
                "lastName": "%s"
            }
        """.formatted(email, password, firstName, lastName);


        // Exécution de la requête POST
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)) // Ajout du corps de la requête
                .andExpect(status().isBadRequest()); // Vérifie que le statut HTTP est 201
                //.andDo(print()); // Affiche la requête et la réponse dans la console
    }
}
