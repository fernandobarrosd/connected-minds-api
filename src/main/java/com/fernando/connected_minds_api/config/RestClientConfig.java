package com.fernando.connected_minds_api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    @Value("${supabase.url}")
    private String supabaseURL;

    @Value("${supabase.jwt-key}")
    private String supabaseJWTKey;

    RestClient restClient() {
        return RestClient.builder()
        .baseUrl(supabaseURL)
        .defaultHeader("Authorization", "Bearer %s".formatted(supabaseJWTKey))
        .build();
    }
}