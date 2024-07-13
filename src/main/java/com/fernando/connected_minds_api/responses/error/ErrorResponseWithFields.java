package com.fernando.connected_minds_api.responses.error;

import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@Getter
public class ErrorResponseWithFields extends ErrorResponse {
    private final List<Map<String, String>> fields;


    public ErrorResponseWithFields(String message, String path, Integer statusCode, LocalDateTime date,
                                   List<Map<String, String>> fields) {
        super(message, path, statusCode, date);
        this.fields = fields;
    }

    public static ErrorResponseWithFieldsBuilder errorResponseWithFieldsBuilder() {
        return new ErrorResponseWithFieldsBuilder();
    }

    public static class ErrorResponseWithFieldsBuilder {
        private List<Map<String, String>> fields;
        private String message;
        private String path;
        private Integer statusCode;
        private LocalDateTime date;

        public ErrorResponseWithFieldsBuilder message(String message) {
            this.message = message;
            return this;
        }

        public ErrorResponseWithFieldsBuilder path(String path) {
            this.path = path;
            return this;
        }

        public ErrorResponseWithFieldsBuilder statusCode(Integer statusCode) {
            this.statusCode = statusCode;
            return this;
        }
        public ErrorResponseWithFieldsBuilder date(LocalDateTime date) {
            this.date = date;
            return this;
        }

        public ErrorResponseWithFieldsBuilder fields(List<Map<String, String>> fields) {
            this.fields = fields;
            return this;
        }

        public ErrorResponseWithFields build() {
            return new ErrorResponseWithFields(
                    path,
                    message,
                    statusCode,
                    date,
                    fields
            );
        }
    }
}