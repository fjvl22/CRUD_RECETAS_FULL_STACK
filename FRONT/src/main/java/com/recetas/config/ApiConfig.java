package com.recetas.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ApiConfig {

    @Bean
    public RestClient restClient(@Value("${recetas.api.url}") String apiUrl) { return RestClient.builder().baseUrl(apiUrl).build(); }
}