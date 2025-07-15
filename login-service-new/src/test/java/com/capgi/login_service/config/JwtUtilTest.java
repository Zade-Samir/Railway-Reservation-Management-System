package com.capgi.login_service.config;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private String email;
    private String token;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();  // Direct instance (no need to mock anything internal)
        email = "test@example.com";

        // Generate a real token using the same method for consistency
        token = jwtUtil.generateToken(email);
    }

    @Test
    void testGenerateToken() {
        String generatedToken = jwtUtil.generateToken(email);
        assertNotNull(generatedToken, "Token should not be null");
        assertFalse(generatedToken.isEmpty(), "Token should not be empty");
    }

    @Test
    void testExtractUsername() {
        String extractedEmail = jwtUtil.extractUsername(token);
        assertEquals(email, extractedEmail, "Extracted email should match original");
    }

    @Test
    void testValidateToken_Valid() {
        boolean isValid = jwtUtil.validateToken(token, email);
        assertTrue(isValid, "Token should be valid");
    }

    @Test
    void testValidateToken_InvalidEmail() {
        boolean isValid = jwtUtil.validateToken(token, "other@example.com");
        assertFalse(isValid, "Token should be invalid for mismatched email");
    }

    @Test
    void testExtractExpiration() {
        Date expiration = jwtUtil.extractExpiration(token);
        assertNotNull(expiration, "Expiration date should not be null");
        assertTrue(expiration.after(new Date()), "Expiration date should be in the future");
    }

    @Test
    void testIsTokenExpired_False() {
        boolean isExpired = jwtUtil.validateToken(token, email);
        assertFalse(!isExpired, "Token should not be expired");
    }

    @Test
    void testInvalidToken_Malformed() {
        String malformedToken = "abc.def.ghi";
        Exception exception = assertThrows(RuntimeException.class, () -> {
            jwtUtil.extractUsername(malformedToken);
        });
    }

    @Test
    void testExpiredToken() {
        // Token valid for -1 second (already expired)
        String expiredToken = Jwts.builder()
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis() - 10000))
                .expiration(new Date(System.currentTimeMillis() - 1000))
                .signWith(Keys.hmacShaKeyFor("mySuperSecureSecretKeyThatIsAtLeast32Chars".getBytes()), Jwts.SIG.HS256)
                .compact();

        Exception exception = assertThrows(RuntimeException.class, () -> {
            jwtUtil.validateToken(expiredToken, email);
        });

    }
}
