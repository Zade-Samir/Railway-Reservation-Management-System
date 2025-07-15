package com.capgi.registration_service.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class SecurityConfigTest {

    private final SecurityConfig securityConfig = new SecurityConfig();

    @Test
    public void testPasswordEncoderBeanCreated() {
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        assertNotNull(encoder, "PasswordEncoder bean should not be null");
        assertTrue(encoder instanceof BCryptPasswordEncoder, "Should be instance of BCryptPasswordEncoder");

        // Optional: Check that encoder encodes correctly and matches
        String rawPassword = "Test123";
        String encodedPassword = encoder.encode(rawPassword);
        assertTrue(encoder.matches(rawPassword, encodedPassword), "Encoded password should match raw password");
    }
}
