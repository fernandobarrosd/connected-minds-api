package com.fernando.connected_minds_api.exceptions;

public class UserIsNotOwnerOfResourceException extends RuntimeException {
    public UserIsNotOwnerOfResourceException(String message) {
        super(message);
    }    
}