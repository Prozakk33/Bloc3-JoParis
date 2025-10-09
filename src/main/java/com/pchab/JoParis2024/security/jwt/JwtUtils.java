package com.pchab.JoParis2024.security.jwt;
import java.security.Key;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.pchab.JoParis2024.security.service.UserDetailsImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@EnableConfigurationProperties
@Component
public class JwtUtils {


    @Value("${JoParis2024.security.secret}")
    private String jwtSecret;

    @Value("${JoParis2024.security.expiration}")
    private long jwtExpirationMs;

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    
    // Valider le token
    public boolean validateJwtToken(String token) {
        System.out.println("-- JWTUtils - Validating JWT token: " + token);
        try {
            // Logique de validation du token
            Jwts.parserBuilder().setSigningKey(key()).build().parse(token);
            return true;
        } catch (MalformedJwtException e) {
            // Gérer les exceptions de validation
            logger.error("-- JWTUtils - Invalid JWT token: {}", e.getMessage());
            return false;
        } catch (ExpiredJwtException e) {
            // Token expiré
            logger.error("-- JWTUtils - JWT token is expired: {}", e.getMessage());
            return false;
        } catch (UnsupportedJwtException e) {
            // Token non supporté
            logger.error("-- JWTUtils - JWT token is unsupported: {}", e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            // Token vide ou null
            logger.error("-- JWTUtils - JWT claims string is empty: {}", e.getMessage());
            return false;
        }
    }

    // Clé secrète pour la signature du token
    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    // Génération du Token
    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        System.out.println("-- JWTUtils - Generating JWT token for user: " + userPrincipal.getUsername());
        System.out.println("-- JWTUtils - Token will expire in (ms): " + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .claim("role", userPrincipal.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getEmailFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public String getFirstNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().get("firstName", String.class);
    }

    public String getLastNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().get("lastName", String.class);
    }

    public String generateUserKeyToken(String email, String firstName, String lastName) {
        return Jwts.builder()
                .setSubject(email)
                .claim("firstName", firstName)
                .claim("lastName", lastName)
                .setIssuedAt(new Date())
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }


    public String generateTicketKeyToken(String firstName, String lastName,  Timestamp buyDate, Long eventId, String ticketType) {
        return Jwts.builder()
                .setSubject("Ticket")
                .claim("firstName", firstName)
                .claim("lastName", lastName)
                .claim("buyDate", buyDate)
                .claim("eventId", eventId)
                .claim("ticketType", ticketType)
                .setIssuedAt(new Date())
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Map<String, Object> decodeTicketToken(String token) {

        System.out.println("-- JWTUtils - Decoding ticket token");
        try {
            var claims = Jwts.parserBuilder().setSigningKey(key()).build()
                    .parseClaimsJws(token).getBody();
            Map<String, Object> ticketData = new HashMap<>();
            ticketData.put("firstName", claims.get("firstName"));
            ticketData.put("lastName", claims.get("lastName"));
            ticketData.put("buyDate", claims.get("buyDate"));
            ticketData.put("ticketType", claims.get("ticketType"));
            ticketData.put("eventId", claims.get("eventId"));
           
            return ticketData;
        } catch (Exception e) {
            logger.error("-- JWTUtils - Error decoding ticket token: {}", e.getMessage());
            return null;
        }
    }

}
