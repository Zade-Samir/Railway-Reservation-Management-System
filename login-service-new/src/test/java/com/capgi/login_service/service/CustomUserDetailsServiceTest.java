package com.capgi.login_service.service;

import com.capgi.login_service.model.User;
import com.capgi.login_service.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initializes mocks and injects
    }

    @Test
    void testLoadUserByUsername_UserFound_ShouldReturnUserDetails() {
        // Arrange
        String email = "user@example.com";
        String password = "securePass";
        User mockUser = new User();
        mockUser.setEmail(email);
        mockUser.setPassword(password);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

        // Act
        UserDetails result = customUserDetailsService.loadUserByUsername(email);

        // Assert
        assertNotNull(result);
        assertEquals(email, result.getUsername());
        assertEquals(password, result.getPassword());
        assertTrue(result.getAuthorities().isEmpty());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testLoadUserByUsername_UserNotFound_ShouldThrowException() {
        // Arrange
        String email = "notfound@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername(email);
        });

        assertEquals("User not found with Email: " + email, exception.getMessage());
        verify(userRepository, times(1)).findByEmail(email);
    }
}
