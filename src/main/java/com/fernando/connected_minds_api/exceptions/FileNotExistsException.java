package com.fernando.connected_minds_api.exceptions;

public class FileNotExistsException extends RuntimeException {
    public FileNotExistsException(String message) {
        super(message);
    }
}