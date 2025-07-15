package com.capgi.registration_service.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capgi.registration_service.dto.UserRegistrationRequest;
import com.capgi.registration_service.model.User;
import com.capgi.registration_service.repository.UserRepository;
import com.capgi.registration_service.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody UserRegistrationRequest request) {
        logger.info("Registering user with email: {}", request.getEmail());
        User registeredUser = userService.registerUser(request);
        logger.info("User registered successfully with ID: {}", registeredUser.getId());
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        try {
            logger.info("Fetching user with ID: {}", id);
            User user = userService.getUserById(id);
            logger.info("Successfully retrieved user with ID: {}", id);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            logger.error("Error fetching user with ID {}: {}", id, e.getMessage(), e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAll() {
        logger.info("Fetching all users");
        List<User> users = userService.getAllUsers();
        logger.info("Total users fetched: {}", users.size());
        return ResponseEntity.ok(users);
    }

    @PostMapping("/validate")
    public ResponseEntity<String> validateUser(@RequestBody UserRegistrationRequest request) {
        try {
            logger.info("Validating credentials for email: {}", request.getEmail());
            Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                    logger.info("User validated successfully: {}", user.getEmail());
                    return ResponseEntity.ok("User is Validated -> " + user.getEmail());
                }
            }
            logger.warn("Invalid credentials for email: {}", request.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        } catch (Exception e) {
            logger.error("Validation error for email {}: {}", request.getEmail(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Validation error: " + e.getMessage());
        }
    }
}
