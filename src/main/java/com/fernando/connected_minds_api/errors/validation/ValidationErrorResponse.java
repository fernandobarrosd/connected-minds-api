package com.fernando.connected_minds_api.errors.validation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class ValidationErrorResponse {
    private final String path;
    private final String message;
    private final LocalDateTime date;
    private final Integer statusCode = HttpStatus.UNPROCESSABLE_ENTITY.value();
    private final List<ValidationErrorField> fields;

    public static ValidationErrorResponseBuilder builder() {
        return new ValidationErrorResponseBuilder();
    }

    public static class ValidationErrorResponseBuilder {
        private String path;
        private String message;
        private LocalDateTime date;
        private List<ValidationErrorField> fields;


        public ValidationErrorResponseBuilder path(String newPath) {
            this.path = newPath;
            return this;
        }

        public ValidationErrorResponseBuilder message(String newMessage) {
            this.message = newMessage;
            return this;
        }

        public ValidationErrorResponseBuilder date(LocalDateTime newDate) {
            this.date = newDate;
            return this;
        }

        public ValidationErrorResponseBuilder fields(List<ValidationErrorField> fields) {
            this.fields = fields;
            return this;
        }

        public ValidationErrorResponse build() {
            return new ValidationErrorResponse(path, message, date, fields);
        }
    }
}
