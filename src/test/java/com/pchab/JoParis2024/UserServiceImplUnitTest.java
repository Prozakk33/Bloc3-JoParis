package com.pchab.JoParis2024;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pchab.JoParis2024.pojo.User;
import com.pchab.JoParis2024.repository.EventRepository;
import com.pchab.JoParis2024.repository.TicketRepository;
import com.pchab.JoParis2024.repository.UserRepository;
import com.pchab.JoParis2024.security.jwt.JwtUtils;
import com.pchab.JoParis2024.security.service.SecurityKey;
import com.pchab.JoParis2024.service.impl.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplUnitTest {
    
    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private SecurityKey securityKey;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Test
    public void testFindUserById() {
        Long userId = 1L;
        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");
        mockUser.setEmail("john.doe@example.com");
        mockUser.setPassword("P@ssword12456");
        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(mockUser));  
        User user = userServiceImpl.findUserById(userId);
        assertEquals("John", user.getFirstName());
    }

    @Test
    public void testFindUserByEmail() {
        String email = "john.doe@example.com";
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");
        mockUser.setEmail(email);
        mockUser.setPassword("P@ssword12456");

        when(userRepository.findByEmail(email)).thenReturn(mockUser);

        User user = userServiceImpl.findUserByEmail(email);
        assertEquals("John", user.getFirstName());
    }

    @Test
    public void testCreateUser() {
        User newUser = new User();
        newUser.setFirstName("Jane");
        newUser.setLastName("Smith");
        newUser.setEmail("jane.smith@example.com");
        newUser.setPassword("P@ssword12456");

        String generatedUserKey = "mocked-user-key";
        when(securityKey.generateSecureKey()).thenReturn(generatedUserKey);
        when(userRepository.save(newUser)).thenReturn(newUser);

        // Appel de la méthode à tester
        userServiceImpl.createUser(newUser);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertEquals("jane.smith@example.com", savedUser.getEmail());
        assertEquals("Jane", savedUser.getFirstName());
        assertEquals("Smith", savedUser.getLastName());
        assertEquals(generatedUserKey, savedUser.getUserKey());
    }
}