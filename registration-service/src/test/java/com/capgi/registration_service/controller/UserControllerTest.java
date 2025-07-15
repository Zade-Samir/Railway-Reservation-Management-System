package com.capgi.registration_service.controller;

import com.capgi.registration_service.dto.UserRegistrationRequest;
import com.capgi.registration_service.model.User;
import com.capgi.registration_service.repository.UserRepository;
import com.capgi.registration_service.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;
    private UserRegistrationRequest request;

    @BeforeEach
    void setUp() {
        user = new User(1L, "John Doe", "john@example.com", "encodedpassword", "USER");

        request = new UserRegistrationRequest();
        request.setFullName("John Doe");
        request.setEmail("john@example.com");
        request.setPassword("password123");
    }

    @Test
    void testRegisterUser() throws Exception {
        Mockito.when(userService.registerUser(any(UserRegistrationRequest.class))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.fullName").value(user.getFullName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));
    }

    @Test
    void testGetUserById() throws Exception {
        Mockito.when(userService.getUserById(1L)).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.fullName").value(user.getFullName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));
    }

    @Test
    void testGetAllUsers() throws Exception {
        Mockito.when(userService.getAllUsers()).thenReturn(Arrays.asList(user));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(user.getId()))
                .andExpect(jsonPath("$[0].fullName").value(user.getFullName()))
                .andExpect(jsonPath("$[0].email").value(user.getEmail()));
    }

    @Test
    void testValidateUser_Success() throws Exception {
        Mockito.when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string(user.getEmail()));
    }

    @Test
    void testValidateUser_Failure_InvalidCredentials() throws Exception {
        Mockito.when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid credentials"));
    }

    @Test
    void testValidateUser_Exception() throws Exception {
        Mockito.when(userRepository.findByEmail(request.getEmail())).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Validation error")));
    }
}
