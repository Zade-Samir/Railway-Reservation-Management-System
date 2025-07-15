package com.capgi.registration_service.service.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.capgi.registration_service.dto.UserRegistrationRequest;
import com.capgi.registration_service.exception.UserAlreadyExistsException;
import com.capgi.registration_service.exception.UserNotFoundException;
import com.capgi.registration_service.model.User;
import com.capgi.registration_service.repository.UserRepository;

import java.util.List;
import java.util.Optional;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository, passwordEncoder);
    }

    @Test
    void testRegisterUser_Success() {
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setFullName("Test User");
        request.setEmail("test@example.com");
        request.setPassword("password123");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setFullName("Test User");
        savedUser.setEmail("test@example.com");
        savedUser.setPassword("encodedPassword");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.registerUser(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test User", result.getFullName());
        verify(userRepository, times(1)).save(any(User.class));
    }




    @Test
    public void testRegisterUser_EmailAlreadyExists() {
        // Prepare test data
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setFullName("John Doe");
        request.setEmail("johndoe@example.com");
        request.setPassword("password123");

        // Mock repository behavior to simulate existing user
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(new User()));

        // Call the method and verify exception
        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class, () -> {
            userService.registerUser(request);
        });

        assertEquals("Email already exists!", exception.getMessage());
    }

    @Test
    public void testGetUserById_Success() {
        // Prepare test data
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setFullName("John Doe");

        // Mock repository behavior
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Call the method
        User retrievedUser = userService.getUserById(userId);

        // Verify assertions
        assertNotNull(retrievedUser);
        assertEquals("John Doe", retrievedUser.getFullName());
    }

    @Test
    public void testGetUserById_UserNotFound() {
        // Prepare test data
        Long userId = 1L;

        // Mock repository behavior to simulate user not found
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Call the method and verify exception
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById(userId);
        });

        assertEquals("User not found with ID: " + userId, exception.getMessage());
    }

    @Test
    public void testGetAllUsers() {
        // Prepare test data
        User user1 = new User();
        user1.setFullName("John Doe");

        User user2 = new User();
        user2.setFullName("Jane Doe");

        // Mock repository behavior
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        // Call the method
        List<User> users = userService.getAllUsers();

        // Verify assertions
        assertEquals(2, users.size());
        assertEquals("John Doe", users.get(0).getFullName());
        assertEquals("Jane Doe", users.get(1).getFullName());
    }
}