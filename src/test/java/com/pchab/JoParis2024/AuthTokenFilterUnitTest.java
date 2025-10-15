package com.pchab.JoParis2024;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.pchab.JoParis2024.security.jwt.AuthTokenFilter;
import com.pchab.JoParis2024.security.jwt.JwtUtils;
import com.pchab.JoParis2024.security.service.UserDetailsImpl;
import com.pchab.JoParis2024.security.service.UserDetailsServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
public class AuthTokenFilterUnitTest {

    @InjectMocks
    private TestableAuthTokenFilter authTokenFilter;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private FilterChain filterChain;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    private static class TestableAuthTokenFilter extends AuthTokenFilter {
        @Mock
        private JwtUtils jwtUtils;

        @Mock
        private UserDetailsService userDetailsService;

        public TestableAuthTokenFilter() {
            // Constructeur vide pour permettre à Mockito d'injecter les dépendances
        }

        @Override
        public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            super.doFilterInternal(request, response, filterChain);
        }
    }

    @Test
    public void testDoFilterInternal_ValidToken() throws Exception {
        // Préparer les données
        String token = "valid-jwt-token";
        String username = "testuser@test.com";

        request.addHeader("Authorization", "Bearer " + token);

        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        
        when(userDetails.getUsername()).thenReturn(username);
        when(jwtUtils.validateJwtToken(token)).thenReturn(true);
        when(jwtUtils.getEmailFromJwtToken(token)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

        // Appeler la méthode à tester
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Vérifier les interactions
        verify(jwtUtils, times(1)).validateJwtToken(token);
        verify(jwtUtils, times(1)).getEmailFromJwtToken(token);
        verify(userDetailsService, times(1)).loadUserByUsername(username);
        verify(filterChain, times(1)).doFilter(request, response);

        // Vérifier que le contexte de sécurité est mis à jour
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(username, SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Test
    public void testDoFilterInternal_InvalidToken() throws Exception {
        // Préparer les données
        String token = "invalid-jwt-token";

        request.addHeader("Authorization", "Bearer " + token);

        when(jwtUtils.validateJwtToken(token)).thenReturn(false);

        // Appeler la méthode à tester
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Vérifier les interactions
        verify(jwtUtils, times(1)).validateJwtToken(token);
        verify(userDetailsService, never()).loadUserByUsername(anyString());
        verify(filterChain, times(1)).doFilter(request, response);

        // Vérifier que le contexte de sécurité n'est pas mis à jour
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}