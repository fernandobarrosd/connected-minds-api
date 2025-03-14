package com.fernando.connected_minds_api.errors;

import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;


public record ErrorResponse(
        String message,
        String path,
        Integer statusCode,
        LocalDateTime date) {

    public static ErrorResponseBuilder builder() {
        return new ErrorResponseBuilder();
    }

    public static class ErrorResponseBuilder {
        private String message;
        private String path;
        private Integer statusCode;
        private LocalDateTime date;

        public ErrorResponseBuilder message(String newMessage) {
            this.message = newMessage;
            return this;
        }

        public ErrorResponseBuilder path(String newPath) {
            this.path = newPath;
            return this;
        }

        public ErrorResponseBuilder statusCode(HttpStatus httpStatus) {
            this.statusCode = httpStatus.value();
            return this;
        }

        public ErrorResponseBuilder date(LocalDateTime newDate) {
            this.date = newDate;
            return this;
        }

        public ErrorResponse build() {
            return new ErrorResponse(message, path, statusCode, date);
        }
    }
}