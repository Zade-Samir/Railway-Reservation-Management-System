package com.capgi.booking_service.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@SpringBootTest
public class SecurityConfigTest {

    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    private SecurityFilterChain securityFilterChain;

    @Autowired
    private AuthenticationManager authenticationManager;

    @TestConfiguration
    static class JwtAuthFilterTestConfig {
    	@Bean
    	public JwtAuthFilter jwtAuthFilter() {
    		return mock(JwtAuthFilter.class);
    	}
    }
    
    @Test
    void testSecurityFilterChainBeanLoads() {
        assertThat(securityFilterChain).isNotNull();
    }

    @Test
    void testAuthenticationManagerBeanLoads() {
        assertThat(authenticationManager).isNotNull();
    }

}
