package com.pchab.JoParis2024;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

import com.pchab.JoParis2024.security.jwt.JwtUtils;
import com.pchab.JoParis2024.security.service.UserDetailsImpl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;




@ExtendWith(MockitoExtension.class)
public class JwtUtilsUnitTest {

    @Mock
    private Logger logger;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private UserDetailsImpl userDetailsImpl;

    @InjectMocks
    private JwtUtils jwtUtils;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "SecretKeyForTestingPurposesOnlySecretKeyForTestingPurposesOnly");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 3600000L);
    }

    @Test
    public void testValidateJwtToken_Success() {

        String validToken = Jwts.builder()
                .setSubject("test@example.com")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 10000)) // Expire dans 10 secondes
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode("SecretKeyForTestingPurposesOnlySecretKeyForTestingPurposesOnly")), SignatureAlgorithm.HS256)
                .compact();

        boolean isValid = jwtUtils.validateJwtToken(validToken);
        assertTrue(isValid);
    }

    @Test
    public void testValidateJwtToken_Invalid() throws Exception {
        String token = "invalid_token";
        boolean isValid = jwtUtils.validateJwtToken(token);
        assertFalse(isValid);
    }

    @Test
    public void testValidateJwtToken_Expired() throws Exception {
        String token = "expired_token";
        boolean isValid = jwtUtils.validateJwtToken(token);
        assertFalse(isValid);
    }

    @Test
    public void testValidateJwtToken_Unsupported() throws Exception {
        String token = "unsupported_token";
        boolean isValid = jwtUtils.validateJwtToken(token);
        assertFalse(isValid);
    }

    @Test
    public void testValidateJwtToken_IllegalArgument() throws Exception {
        String token = "";
        boolean isValid = jwtUtils.validateJwtToken(token);
        assertFalse(isValid);
    }

    @Test
    public void testGenerateJwtToken_Success() {
        
        Authentication mockAuthentication = org.mockito.Mockito.mock(Authentication.class);
        Collection<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

        UserDetailsImpl mockUserDetails = new UserDetailsImpl("test@test.test", "P@assword123456", authorities);

        when(mockAuthentication.getPrincipal()).thenReturn(mockUserDetails);

        // Action
        String token = jwtUtils.generateJwtToken(mockAuthentication);

        // Tests
        assertNotNull(token);

        // Décoder le token pour vérifier son contenu
        var claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode("SecretKeyForTestingPurposesOnlySecretKeyForTestingPurposesOnly")))
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals("test@test.test", claims.getSubject()); // Vérifier le sujet
        assertNotNull(claims.getIssuedAt()); // Vérifier la date d'émission
        assertNotNull(claims.getExpiration()); // Vérifier la date d'expiration
        assertTrue(claims.getExpiration().after(claims.getIssuedAt())); // Vérifier que l'expiration est après l'émission
        assertEquals("[{authority=ROLE_USER}]", claims.get("role").toString()); // Vérifier les rôles
        
        
    }

    @Test
    public void testGetEmailFromJwtToken() {
        String token = Jwts.builder()
                .setSubject("test@example.com")
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode("SecretKeyForTestingPurposesOnlySecretKeyForTestingPurposesOnly")), SignatureAlgorithm.HS256)
                .compact();

        String email = jwtUtils.getEmailFromJwtToken(token);
        assertEquals("test@example.com", email);
    }

    @Test
    public void testGetFirstNameFromJwtToken() {
        String token = Jwts.builder()
                .claim("firstName", "John")
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode("SecretKeyForTestingPurposesOnlySecretKeyForTestingPurposesOnly")), SignatureAlgorithm.HS256)
                .compact();

        String firstName = jwtUtils.getFirstNameFromJwtToken(token);
        assertEquals("John", firstName);
    }

    @Test
    public void testGetLastNameFromJwtToken() {
        String token = Jwts.builder()
                .claim("lastName", "Doe")
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode("SecretKeyForTestingPurposesOnlySecretKeyForTestingPurposesOnly")), SignatureAlgorithm.HS256)
                .compact();

        String lastName = jwtUtils.getLastNameFromJwtToken(token);
        assertEquals("Doe", lastName);
    }

    @Test
    public void testGenerateUserKeyToken() {
        String token = jwtUtils.generateUserKeyToken("test@example.com", "John", "Doe");
        assertNotNull(token);
        // Décoder le token pour vérifier son contenu
        var claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode("SecretKeyForTestingPurposesOnlySecretKeyForTestingPurposesOnly")))
                .build()
                .parseClaimsJws(token)
                .getBody();
        assertEquals("test@example.com", claims.getSubject());
        assertEquals("John", claims.get("firstName"));
        assertEquals("Doe", claims.get("lastName"));
    }

    @Test
    public void testGenerateTicketKeyToken() {
        Date now = new Date();
        String token = jwtUtils.generateTicketKeyToken("John", "Doe", new java.sql.Timestamp(now.getTime()), 1L, "VIP");
        assertNotNull(token);
        // Décoder le token pour vérifier son contenu
        var claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode("SecretKeyForTestingPurposesOnlySecretKeyForTestingPurposesOnly")))
                .build()
                .parseClaimsJws(token)
                .getBody();
        assertEquals("Ticket", claims.getSubject());
        assertEquals("John", claims.get("firstName"));  
        assertEquals("Doe", claims.get("lastName"));
        assertEquals(1, ((Number)claims.get("eventId")).longValue());
    }

    @Test
    public void testDecodeTicketToken() {
        Date now = new Date();
        String token = jwtUtils.generateTicketKeyToken("John", "Doe", new java.sql.Timestamp(now.getTime()), 1L, "VIP");
        var ticketData = jwtUtils.decodeTicketToken(token);
        assertNotNull(ticketData);
        assertEquals("John", ticketData.get("firstName"));
        assertEquals("Doe", ticketData.get("lastName"));
        assertEquals(1, ((Number)ticketData.get("eventId")).longValue());
    }
}
