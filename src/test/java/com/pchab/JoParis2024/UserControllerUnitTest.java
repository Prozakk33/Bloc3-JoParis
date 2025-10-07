package com.pchab.JoParis2024;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.pchab.JoParis2024.controller.UserController;
import com.pchab.JoParis2024.pojo.User;
import com.pchab.JoParis2024.security.jwt.JwtUtils;
import com.pchab.JoParis2024.service.UserService;



@ExtendWith(MockitoExtension.class)
public class UserControllerUnitTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Mock
    private JwtUtils jwtUtils;


    // Test for findUserById
    @Test
    public void testFindUserById_Success() throws Exception {
    
        // Data preparation
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("P@ssword123456");
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");
        mockUser.setRole("USER");
        mockUser.setUserKey("uniqueUserKey123");

        // Mocking service layer
        when(userService.findUserById(mockUser.getId())).thenReturn(mockUser);

        // Call the controller method
        User returnedUser = userController.findUserById(mockUser.getId());

        //Asserts
        if (returnedUser != null) {
            assertThat(returnedUser.getEmail()).isEqualTo("test@example.com");
        } else {
            fail();
        }
    }

    @Test
    public void testFindUserById_NotFound() throws Exception {
    
        // Data preparation
        Long userId = 999L;

        // Mocking service layer
        when(userService.findUserById(userId)).thenReturn(null);

        // Call the controller method
        User returnedUser = userController.findUserById(userId);

        //Asserts
        assertThat(returnedUser).isNull();
    }
    // -------------------------------------------------------------------------
    // Test for findByEmail
    @Test
    public void testFindByEmail_Success() throws Exception {
    
        // Data preparation
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("P@ssword123456");
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");
        mockUser.setRole("USER");
        mockUser.setUserKey("uniqueUserKey123");

        // Mocking service layer
        when(userService.findUserByEmail(mockUser.getEmail())).thenReturn(mockUser);

        // Call the controller method
        User returnedUser = userController.findByEmail(mockUser.getEmail());

        //Asserts
        if (returnedUser != null) {
            assertThat(returnedUser.getEmail()).isEqualTo("test@example.com");
        } else {
            fail();
        }
    }

    @Test
    public void testFindByEmail_NotFound() throws Exception {
    
        // Data preparation
        String email = "notfound@example.com";
        // Mocking service layer
        when(userService.findUserByEmail(email)).thenReturn(null);
        // Call the controller method
        User returnedUser = userController.findByEmail(email);
        //Asserts
        assertThat(returnedUser).isNull();
    }

    @Test
    public void testGetUserByToken_Success() throws Exception {
    
        // Data preparation
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("P@ssword123456");
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");
        mockUser.setRole("USER");
        mockUser.setUserKey("uniqueUserKey123");

        String jwtToken = "someJwtToken";

        String AuthorizationHeader = "Bearer " + jwtToken;

        // Mocking service layer
        when(jwtUtils.getEmailFromJwtToken(jwtToken)).thenReturn(mockUser.getEmail());
        when(jwtUtils.validateJwtToken(jwtToken)).thenReturn(true);
        when(userService.findUserByEmail(mockUser.getEmail())).thenReturn(mockUser);

        // Call the controller method
        ResponseEntity<User> response = userController.getUserFromToken(AuthorizationHeader);

        //Asserts
        if (response != null) {
            System.out.println("Response Status Code: " + response.getStatusCode());
            System.out.println("Response Body: " + response.getBody()); 
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody().getEmail()).isEqualTo("test@example.com");
        } else {
            fail();
        }

    }
}
