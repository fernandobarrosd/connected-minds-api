package com.fernando.connected_minds_api.exceptions;

public class UserIsAlreadyExiststInCommunityOrGroupException extends RuntimeException {
    public UserIsAlreadyExiststInCommunityOrGroupException(String message) {
        super(message);
    }
}