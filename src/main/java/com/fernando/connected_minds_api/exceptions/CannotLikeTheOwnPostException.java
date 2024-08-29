package com.fernando.connected_minds_api.exceptions;

public class CannotLikeTheOwnPostException extends RuntimeException {
    public CannotLikeTheOwnPostException(String message) {
        super(message);
    }   
}