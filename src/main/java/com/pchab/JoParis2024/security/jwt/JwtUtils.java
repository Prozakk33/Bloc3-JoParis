package com.pchab.JoParis2024.security.jwt;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import java.security.Key;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import com.pchab.JoParis2024.security.service.UserDetailsImpl;
import java.util.Date;
import io.jsonwebtoken.SignatureAlgorithm;



@Component
public class JwtUtils {

    // Génération du Token
    @Value("${paris.security.jwtSecret}")
    private String jwtSecret;

    @Value("${paris.security.jwtExpirationMs}")
    private int jwtExpirationMs;
    // Récupérer l'email dans le token

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    
    // Valider le token
    public boolean validateJwtToken(String token) {
        try {
            // Logique de validation du token
            Jwts.parser().setSigningKey(key()).build().parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            // Gérer les exceptions de validation
            logger.error("Invalid JWT token: {}", e.getMessage());
            return false;
        } catch (ExpiredJwtException e) {
            // Token expiré
            logger.error("JWT token is expired: {}", e.getMessage());
            return false;
        } catch (UnsupportedJwtException e) {
            // Token non supporté
            logger.error("JWT token is unsupported: {}", e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            // Token vide ou null
            logger.error("JWT claims string is empty: {}", e.getMessage());
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
        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getEmailFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }
}
