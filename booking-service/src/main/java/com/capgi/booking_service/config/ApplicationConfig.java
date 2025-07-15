package com.capgi.booking_service.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class ApplicationConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);

    @Bean
    public UserDetailsService userDetailsService() {
    	logger.info("Creating userDetailsService bean with dummy credentials.");
        return username -> User
                .withUsername(username)
                .password(passwordEncoder().encode("dummy"))
                .roles("USER")
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
    	logger.info("Creating DaoAuthenticationProvider bean.");
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
    	logger.info("Creating BCryptPasswordEncoder bean.");
        return new BCryptPasswordEncoder();
    }
}
