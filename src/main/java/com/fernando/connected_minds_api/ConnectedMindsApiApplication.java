package com.fernando.connected_minds_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.boot.CommandLineRunner;
import com.fernando.connected_minds_api.services.AuthService;
import com.fernando.connected_minds_api.requests.RegisterRequest;
import org.springframework.context.annotation.Bean;
import lombok.RequiredArgsConstructor;


@SpringBootApplication
@EnableCaching
@RequiredArgsConstructor
public class ConnectedMindsApiApplication {
    private final AuthService authService;

    public static void main(String[] args) {
        SpringApplication.run(ConnectedMindsApiApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> {
            RegisterRequest user = new RegisterRequest(
                "fernandobarrosd",
                "fbarros@test.com",
                "fbarros123",
                null,
                null,
                "MALE",
                "2004-10-09"
            );

            authService.registerUser(user);
        };
    }

}