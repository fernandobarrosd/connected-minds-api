package com.fernando.connected_minds_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ConnectedMindsApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConnectedMindsApiApplication.class, args);
    }
}