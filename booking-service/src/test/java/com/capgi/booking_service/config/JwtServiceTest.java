package com.capgi.booking_service.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.security.Key;
import java.util.Date;

public class JwtServiceTest {

    private JwtService jwtService;
    private String validToken;
    private String expiredToken;

    private static final String SECRET_KEY = "mySuperSecureSecretKeyThatIsAtLeast32Chars"; // Secret Key for signing

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();

        long now = System.currentTimeMillis();
        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

        // Create a valid JWT token
        validToken = Jwts.builder()
                .setSubject("testuser")
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + 1000 * 60 * 60)) // 1 hour validity
                .signWith(key, SignatureAlgorithm.HS256)      
                .compact();

        // Create an expired JWT token
        expiredToken = Jwts.builder()
                .setSubject("expireduser")
                .setIssuedAt(new Date(now - 1000 * 60 * 60 * 2)) // 2 hours ago
                .setExpiration(new Date(now - 1000 * 60 * 60))   // 1 hour ago
                .signWith(key, SignatureAlgorithm.HS256)       
                .compact();
    }

    @Test
    void testExtractUsername() {
        String username = jwtService.extractUsername(validToken);
        assertEquals("testuser", username, "Username should match the subject of the token");
    }

    @Test
    void testIsTokenValid_ValidToken() {
        boolean isValid = jwtService.isTokenValid(validToken);
        assertTrue(isValid, "Token should be valid");
    }

    @Test
    void testIsTokenValid_ExpiredToken() {
        boolean isValid = jwtService.isTokenValid(expiredToken);
        assertFalse(isValid, "Token should be invalid due to expiration");
    }

    @Test
    void testExtractClaim() {
        String username = jwtService.extractClaim(validToken, claims -> claims.getSubject());
        assertEquals("testuser", username, "Claim extraction should return correct username");
    }

    @Test
    void testTokenValidation_InvalidToken() {
        String invalidToken = "invalid.token.here";
        boolean isValid = jwtService.isTokenValid(invalidToken);
        assertFalse(isValid, "Invalid token should return false");
    }
}
