package com.capgi.booking_service.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.PrintWriter;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class JwtAuthFilterTest {

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldAuthenticateWhenTokenIsValid() throws Exception {
        String token = "valid.jwt.token";
        String username = "testuser";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extractUsername(token)).thenReturn(username);
        when(jwtService.isTokenValid(token)).thenReturn(true);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void shouldSkipAuthenticationWhenNoAuthHeader() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void shouldRejectExpiredToken() throws Exception {
        String token = "expired.token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extractUsername(token))
        			.thenThrow(
		        		new io.jsonwebtoken.ExpiredJwtException(null, null, "Token expired")
		        		);
        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(writer).write("Token expired");
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void shouldRejectInvalidToken() throws Exception {
        String token = "invalid.token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extractUsername(token)).thenThrow(new io.jsonwebtoken.JwtException("Invalid token"));
        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(writer).write("Invalid token");
        verify(filterChain, never()).doFilter(request, response);
    }
}
