package com.fernando.connected_minds_api.exceptions;

public class JWTTokenInvalidException extends RuntimeException {
    public JWTTokenInvalidException(String message) {
        super(message);
    }
}