package com.capgi.login_service.service;

import com.capgi.login_service.config.JwtUtil;
import com.capgi.login_service.model.AuthRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClientMock;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        when(webClientBuilder.build()).thenReturn(webClientMock);

        // Mock WebClient chain
        WebClient.RequestBodyUriSpec requestBodyUriSpecMock = mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestBodySpec requestBodySpecMock = mock(WebClient.RequestBodySpec.class);
        WebClient.RequestHeadersSpec requestHeadersSpecMock = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpecMock = mock(WebClient.ResponseSpec.class);

        lenient().when(webClientMock.post()).thenReturn(requestBodyUriSpecMock);
        lenient().when(requestBodyUriSpecMock.uri(anyString())).thenReturn(requestBodySpecMock);
        lenient().when(requestBodySpecMock.bodyValue(any())).thenReturn(requestHeadersSpecMock);
        lenient().when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
        lenient().when(responseSpecMock.bodyToMono(String.class)).thenReturn(Mono.just("user@example.com"));

        lenient().when(jwtUtil.generateToken("user@example.com")).thenReturn("mock-token");
    }


    @Test
    public void testAuthenticateAndGenerateToken_RegistrationServiceError() {
        // Arrange
        AuthRequest request = new AuthRequest("user@example.com", "password123");

        // Simulate an error from the WebClient call (e.g., registration service is down)
        when(webClientMock.post()
                .uri(anyString())
                .bodyValue(any())
                .retrieve()
                .bodyToMono(String.class))
                .thenReturn(Mono.error(new WebClientResponseException("Service Unavailable", 503, "Service Unavailable", null, null, null)));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.authenticateAndGenerateToken(request);
        });

//        assertTrue(exception.getMessage().contains("User validation failed"));
        verify(jwtUtil, never()).generateToken(anyString());
    }

    @Test
    public void testAuthenticateAndGenerateToken_GenericError() {
        // Arrange
        AuthRequest request = new AuthRequest("user@example.com", "password123");

        // Simulate a generic exception (e.g., unknown error)
        when(webClientMock.post()
                .uri(anyString())
                .bodyValue(any())
                .retrieve()
                .bodyToMono(String.class))
                .thenReturn(Mono.error(new Exception("Unexpected error")));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.authenticateAndGenerateToken(request);
        });

//        assertTrue(exception.getMessage().contains("An unexpected error occurred during authentication"));
        verify(jwtUtil, never()).generateToken(anyString());
    }
}
