package com.fernando.connected_minds_api.user;

public class UserUnderAgeException extends RuntimeException {
    public UserUnderAgeException(String message) {
        super(message);
    }
}