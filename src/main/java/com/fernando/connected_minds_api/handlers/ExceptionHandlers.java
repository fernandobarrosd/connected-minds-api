package com.fernando.connected_minds_api.handlers;

import com.fernando.connected_minds_api.errors.ErrorResponse;
import com.fernando.connected_minds_api.errors.validation.ValidationErrorField;
import com.fernando.connected_minds_api.errors.validation.ValidationErrorResponse;
import com.fernando.connected_minds_api.exceptions.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class ExceptionHandlers {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(
            EntityNotFoundException exception,
            HttpServletRequest request) {

        int statusCode = HttpStatus.NOT_FOUND.value();
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(exception.getMessage())
                .statusCode(HttpStatus.NOT_FOUND)
                .path(request.getRequestURI())
                .date(LocalDateTime.now())
                .build();

        return ResponseEntity.status(statusCode).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpServletRequest request) {

        List<ValidationErrorField> fields = exception.getFieldErrors()
                .stream()
                .map(this::convertToValidationErrorField)
                .toList();

        var errorResponse = ValidationErrorResponse.builder()
                .message("Validation error")
                .path(request.getRequestURI())
                .date(LocalDateTime.now())
                .fields(fields)
                .build();

        return ResponseEntity.unprocessableEntity().body(errorResponse);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ValidationErrorResponse> handleHandlerMethodValidationException(
            HandlerMethodValidationException exception,
            HttpServletRequest request) {
        List<ValidationErrorField> fields = exception.getValueResults()
                .stream()
                .map(this::convertToValidationErrorField)
                .toList();

        var errorResponse = ValidationErrorResponse.builder()
                .message("Validation error")
                .path(request.getRequestURI())
                .date(LocalDateTime.now())
                .fields(fields)
                .build();

        return ResponseEntity.unprocessableEntity().body(errorResponse);
    }

    private ValidationErrorField convertToValidationErrorField(FieldError fieldError) {
        return ValidationErrorField.builder()
                .field(fieldError.getField())
                .message(fieldError.getDefaultMessage())
                .build();
    }

    private ValidationErrorField convertToValidationErrorField(ParameterValidationResult parameterValidation) {
        return ValidationErrorField.builder()
                .field(parameterValidation.getMethodParameter().getParameterName())
                .message(parameterValidation.getResolvableErrors().get(0).getDefaultMessage())
                .build();
    }

}