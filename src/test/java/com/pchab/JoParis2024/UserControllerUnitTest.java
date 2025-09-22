package com.pchab.JoParis2024;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import com.pchab.JoParis2024.service.UserService;

import jakarta.inject.Inject;

public class UserControllerUnitTest {

    @ExtendWith(MockitoExtension.class)
    public class UserControllerUnitTest {

        @Mock
        private UserService userService;

        @InjectMocks
        private UserController userController;

        @Test
        void testGetUserById() {
            // Given
            Long userId = 1L;
            // User Creation
            User mockUser = new User();
            mockUser.setId(userId);
            mockUser.setFirstName("John");
            mockUser.setLastName("Doe");
            mockUser.setEmail("test@test.fr");
            mockUser.setPassword("P@ssword123456");

            // Test
            when(userService.findUserById(userId)).thenReturn(mockUser);

            // When
            User response = userController.findUserById(userId);

            // Then
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(mockUser, response);
        }

        @Test
        void testFindByEmail() {
            // Given
            String email = "test@test.fr";
            User mockUser = new User();
            mockUser.setEmail(email);
            when(userService.findUserByEmail(email)).thenReturn(mockUser);

            // When
            User response = userController.findByEmail(email);

            // Then
            assertEquals(mockUser, response);
        }
    }
}
