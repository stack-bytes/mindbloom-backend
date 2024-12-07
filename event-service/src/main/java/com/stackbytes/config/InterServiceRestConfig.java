package com.stackbytes.config;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpClient;

@Configuration
public class InterServiceRestConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
