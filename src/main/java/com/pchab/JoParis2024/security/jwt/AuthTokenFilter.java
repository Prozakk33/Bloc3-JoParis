package com.pchab.JoParis2024.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.pchab.JoParis2024.security.service.UserDetailsImpl;
import com.pchab.JoParis2024.security.service.UserDetailsServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class AuthTokenFilter extends OncePerRequestFilter {


    @Autowired
    private JwtUtils JwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws java.io.IOException, ServletException {
        
            try {
                String jwt = parseJwt(request);
                if (jwt != null && JwtUtils.validateJwtToken(jwt)) {
                    String email = JwtUtils.getEmailFromJwtToken(jwt);

                    // Load user details using email
                    UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(email);

                    // Create authentication token
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Set authentication in context
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                System.err.println("Cannot set user authentication: " + e.getMessage());
            }
    }
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if(StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            // Découpage du Token
            return headerAuth.substring(7);
        }
        return null;
    }   
    
}
