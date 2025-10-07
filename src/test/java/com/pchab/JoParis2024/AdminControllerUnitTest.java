
package com.pchab.JoParis2024;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pchab.JoParis2024.controller.UserController;
import com.pchab.JoParis2024.security.jwt.JwtUtils;
import com.pchab.JoParis2024.service.UserService;






@ExtendWith(MockitoExtension.class)
public class AdminControllerUnitTest {
    @Mock
    private UserService userService;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {
        //userController = new UserController(userService, jwtUtils);
    }
    


}