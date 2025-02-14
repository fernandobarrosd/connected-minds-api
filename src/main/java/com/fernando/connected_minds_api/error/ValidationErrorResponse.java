package com.fernando.connected_minds_api.error;

import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@Getter
public class ValidationErrorResponse extends ErrorResponse {
    private final List<Map<String, String>> fields;


    public ValidationErrorResponse(String message, String path, Integer statusCode, LocalDateTime date,
                                   List<Map<String, String>> fields) {
        super(message, path, statusCode, date);
        this.fields = fields;
    }

    public static ValidationErrorResponseBuilder validationErrorResponseBuilder() {
        return new ValidationErrorResponseBuilder();
    }

    public static class ValidationErrorResponseBuilder {
        private List<Map<String, String>> fields;
        private String message;
        private String path;
        private Integer statusCode;
        private LocalDateTime date;

        public ValidationErrorResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        public ValidationErrorResponseBuilder path(String path) {
            this.path = path;
            return this;
        }

        public ValidationErrorResponseBuilder statusCode(Integer statusCode) {
            this.statusCode = statusCode;
            return this;
        }
        public ValidationErrorResponseBuilder date(LocalDateTime date) {
            this.date = date;
            return this;
        }

        public ValidationErrorResponseBuilder fields(List<Map<String, String>> fields) {
            this.fields = fields;
            return this;
        }

        public ValidationErrorResponse build() {
            return new ValidationErrorResponse(
                    path,
                    message,
                    statusCode,
                    date,
                    fields
            );
        }
    }
}