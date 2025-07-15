package com.capgi.registration_service.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.capgi.registration_service.dto.UserRegistrationRequest;
import com.capgi.registration_service.exception.UserAlreadyExistsException;
import com.capgi.registration_service.exception.UserNotFoundException;
import com.capgi.registration_service.model.User;
import com.capgi.registration_service.repository.UserRepository;
import com.capgi.registration_service.service.UserService;

import lombok.RequiredArgsConstructor;

//Spring automatically injects the only class that implements that interface — in this case, UserServiceImpl.
//hence, we are using UserServiceImpl that interfaces the UserService

@Service
@RequiredArgsConstructor // Automatically injects required constructor injections  
public class UserServiceImpl implements UserService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Service interface for encoding password
    
    // Register new user
    @Override
    public User registerUser(UserRegistrationRequest request) {
        
        logger.info("Attempting to register user with email: {}", request.getEmail());
        
        // Validate email and password
        if (!StringUtils.hasText(request.getEmail()) || !StringUtils.hasText(request.getPassword())) {
            logger.error("Email or password cannot be empty for registration.");
            throw new IllegalArgumentException("Email and password must not be empty.");
        }
        
        // Check if user already exists
        if(userRepository.findByEmail(request.getEmail()).isPresent()) {
            logger.error("Registration failed: Email {} already exists.", request.getEmail());
            throw new UserAlreadyExistsException("Email already exists!");
        }
        
        // If the user does not exist, create a new user
        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Convert into hashed string
        user.setRole("USER");
        
        // Save the user and log the success
        User savedUser = userRepository.save(user);
        logger.info("User successfully registered with ID: {}", savedUser.getId());
        return savedUser;
    }
    
    // Retrieve user by ID
    @Override
    public User getUserById(Long id) {
        logger.info("Fetching user by ID: {}", id);
        
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("User not found with ID: {}", id);
                    return new UserNotFoundException("User not found with ID: " + id);
                });
    }
    
    // Retrieve all users
    @Override
    public List<User> getAllUsers() {
        logger.info("Fetching all users.");
        List<User> users = userRepository.findAll();
        logger.info("Fetched {} users.", users.size());
        return users;
    }
}
