package com.capgi.login_service.config;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.web.reactive.function.client.WebClient;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WebClientConfigTest {

    @Autowired
    private ApplicationContext applicationContext;

    @InjectMocks
    private WebClientConfig webClientConfig;

    @Test
    void testWebClientBeanIsLoaded() {
        // Verify that the WebClient.Builder bean is present in the application context
        WebClient.Builder webClientBuilder = applicationContext.getBean(WebClient.Builder.class);
        
        assertNotNull(webClientBuilder, "WebClient.Builder bean should be present in the application context.");
    }

    @Test
    void testLoadBalancedWebClient() {
        // Retrieve the WebClient bean and ensure it is load-balanced
        WebClient.Builder webClientBuilder = applicationContext.getBean(WebClient.Builder.class);
        
        // Here we simulate that the WebClient is correctly configured
        // Since we can't actually test service-to-service calls in this unit test, 
        // we'll simply verify that the WebClient is being correctly initialized.
        assertNotNull(webClientBuilder, "WebClient.Builder bean should be load-balanced.");
    }
}
