package com.capgi.login_service.config;

import com.capgi.login_service.service.CustomUserDetailsService;
import com.capgi.login_service.service.JwtFilter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

class SecurityConfigUnitTest {

    @Mock
    private JwtFilter jwtFilter;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private AuthenticationConfiguration authenticationConfiguration;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPasswordEncoderBean() {
        BCryptPasswordEncoder encoder = securityConfig.passwordEncoder();
        assertNotNull(encoder, "BCryptPasswordEncoder bean should not be null");

        String rawPassword = "testPassword";
        String encodedPassword = encoder.encode(rawPassword);

        assertTrue(encoder.matches(rawPassword, encodedPassword), "Encoded password should match raw password");
    }

    @Test
    void testUserDetailsServiceBean() {
        when(customUserDetailsService.loadUserByUsername("test")).thenReturn(null); // Optional mocking
        UserDetailsService userDetailsService = securityConfig.userDetailsService();
        assertNotNull(userDetailsService, "UserDetailsService should not be null");
    }

    @Test
    void testAuthenticationManagerBean() throws Exception {
        when(authenticationConfiguration.getAuthenticationManager()).thenReturn(authenticationManager);

        AuthenticationManager manager = securityConfig.authenticationManager(authenticationConfiguration);
        assertNotNull(manager, "AuthenticationManager should not be null");

        verify(authenticationConfiguration, times(1)).getAuthenticationManager();
    }

    @Test
    void testSecurityFilterChainBean() throws Exception {
        HttpSecurity httpSecurity = mock(HttpSecurity.class, RETURNS_DEEP_STUBS);

        // Configure HttpSecurity mock behavior if needed
        // Just verify it doesn't throw exception and returns non-null
        SecurityFilterChain filterChain = securityConfig.securityFilterChain(httpSecurity);
        assertNotNull(filterChain, "SecurityFilterChain should not be null");
    }
}
