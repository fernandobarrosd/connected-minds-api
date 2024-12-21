package com.fernando.connected_minds_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import com.fernando.connected_minds_api.services.AuthService;
import com.fernando.connected_minds_api.utils.JSONReader;
import com.fernando.connected_minds_api.requests.RegisterRequest;
import org.springframework.context.annotation.Bean;
import lombok.RequiredArgsConstructor;
import java.util.Arrays;


@SpringBootApplication
@RequiredArgsConstructor
public class ConnectedMindsApiApplication {
    private final AuthService authService;

    public static void main(String[] args) {
        SpringApplication.run(ConnectedMindsApiApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> {
            RegisterRequest[] users = JSONReader.readJSON(RegisterRequest[].class, "users.json");

            Arrays.stream(users)
            .forEach(authService::registerUser);  
        };
    }

}