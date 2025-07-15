package com.capgi.registration_service.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // Logger for this class
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    // No login form, no session
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        try {
            return http.csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/api/login", "/api/validate", "/api/register", "/api/users/**")
                            .permitAll()
                            .anyRequest()
                            .authenticated())
                    .build();
        } catch (Exception e) {
            logger.error("Error configuring security filters: ", e);
            throw new RuntimeException("Error configuring security filters", e);
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.info("Password encoder bean created with BCryptPasswordEncoder.");
        return new BCryptPasswordEncoder();
    }
}
