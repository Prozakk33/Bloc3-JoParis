package com.pchab.JoParis2024;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.pchab.JoParis2024.controller.AuthController;
import com.pchab.JoParis2024.pojo.User;
import com.pchab.JoParis2024.security.jwt.JwtUtils;
import com.pchab.JoParis2024.service.UserService;



@ExtendWith(MockitoExtension.class)
public class AuthControllerUnitTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthController authController;

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

        when(jwtUtils.getEmailFromJwtToken(jwtToken)).thenReturn("testuser@example.com");
        when(userService.findUserByEmail("testuser@example.com")).thenReturn(mockUser);

        ResponseEntity<?> response = authController.account(authorizationHeader);

        System.out.println("Response: " + response.toString());
        assert(response.getStatusCode().is2xxSuccessful());
        assert(response.getBody() != null);
        // Simulation du comportement du service
        //when(authController.account(authorizationHeader)).thenReturn(ResponseEntity.ok(new JwtResponse(jwtToken)));


    }

    @Test
    public void testAccount_Failure() {

        // Implement your test logic here
    }

// ----------------------------------------------------------------
    @Test
    public void testSignIn_Success() {
        // Implement your test logic here
    }

    @Test
    public void testSignIn_Failure() {
        // Implement your test logic here
    }

// ---------------------------------------------------------------- 

    @Test
    public void testSignUp_Success() {
        // Implement your test logic here
    }
    @Test
    public void testSignUp_Failure() {
        // Implement your test logic here
    }
}
