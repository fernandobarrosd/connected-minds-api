package com.fernando.connected_minds_api.handlers;

import java.time.LocalDateTime;
import java.util.List;
import com.fernando.connected_minds_api.error.ValidationErrorField;
import com.fernando.connected_minds_api.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.fernando.connected_minds_api.error.ErrorResponse;
import com.fernando.connected_minds_api.error.ValidationErrorResponse;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandlers {
        @ExceptionHandler(EntityNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleEntityNotFound(
                        EntityNotFoundException exception,
                        HttpServletRequest request) {

                int statusCode = HttpStatus.NOT_FOUND.value();
                ErrorResponse errorResponse = ErrorResponse.builder()
                                .message(exception.getMessage())
                                .statusCode(statusCode)
                                .path(request.getRequestURI())
                                .date(LocalDateTime.now())
                                .build();
                return ResponseEntity.status(statusCode).body(errorResponse);
        }

        @ExceptionHandler(FileNotExistsException.class)
        public ResponseEntity<ErrorResponse> handleFileNotExists(
                FileNotExistsException exception,
                HttpServletRequest request) {
                int statusCode = HttpStatus.NOT_FOUND.value();
                ErrorResponse errorResponse = ErrorResponse.builder()
                        .message(exception.getMessage())
                        .statusCode(statusCode)
                        .path(request.getRequestURI())
                        .date(LocalDateTime.now())
                        .build();
                return ResponseEntity.status(statusCode).body(errorResponse);
        }

        @ExceptionHandler(JWTTokenInvalidException.class)
        public ResponseEntity<ErrorResponse> handleJWTTokenInvalid(
                        JWTTokenInvalidException exception,
                        HttpServletRequest request) {

                int statusCode = HttpStatus.BAD_REQUEST.value();
                ErrorResponse errorResponse = ErrorResponse.builder()
                                .message(exception.getMessage())
                                .statusCode(statusCode)
                                .path(request.getRequestURI())
                                .date(LocalDateTime.now())
                                .build();
                return ResponseEntity.badRequest().body(errorResponse);
        }

        @ExceptionHandler(UserIsNotOwnerOfResourceException.class)
        public ResponseEntity<ErrorResponse> handleUserIsNotOwnerOfResource(
                        UserIsNotOwnerOfResourceException exception,
                        HttpServletRequest request) {

                int statusCode = HttpStatus.FORBIDDEN.value();
                ErrorResponse errorResponse = ErrorResponse.builder()
                                .message(exception.getMessage())
                                .statusCode(statusCode)
                                .path(request.getRequestURI())
                                .date(LocalDateTime.now())
                                .build();
                return ResponseEntity.status(statusCode).body(errorResponse);
        }

        @ExceptionHandler(CannotLikeTheOwnPostException.class)
        public ResponseEntity<ErrorResponse> handleCannotLikeTheOwnPost(
                        CannotLikeTheOwnPostException exception,
                        HttpServletRequest request) {

                int statusCode = HttpStatus.FORBIDDEN.value();
                ErrorResponse errorResponse = ErrorResponse.builder()
                                .message(exception.getMessage())
                                .statusCode(statusCode)
                                .path(request.getRequestURI())
                                .date(LocalDateTime.now())
                                .build();
                return ResponseEntity.status(statusCode).body(errorResponse);
        }

        @ExceptionHandler(UserIsAlreadyExiststInCommunityOrGroupException.class)
        public ResponseEntity<ErrorResponse> handleUserIsAlreadyExistsInCommunityOrGroup(
                        UserIsAlreadyExiststInCommunityOrGroupException exception,
                        HttpServletRequest request) {

                int statusCode = HttpStatus.BAD_REQUEST.value();

                ErrorResponse errorResponse = ErrorResponse.builder()
                                .message(exception.getMessage())
                                .statusCode(statusCode)
                                .path(request.getRequestURI())
                                .date(LocalDateTime.now())
                                .build();
                return ResponseEntity.badRequest().body(errorResponse);
        }

        @ExceptionHandler(EntityAlreadyExistsException.class)
        public ResponseEntity<ErrorResponse> handleEntityAlreadyExists(
                        EntityAlreadyExistsException exception,
                        HttpServletRequest request) {

                int statusCode = HttpStatus.CONFLICT.value();
                ErrorResponse errorResponse = ErrorResponse.builder()
                                .message(exception.getMessage())
                                .statusCode(statusCode)
                                .path(request.getRequestURI())
                                .date(LocalDateTime.now())
                                .build();
                return ResponseEntity.status(statusCode).body(errorResponse);
        }

        @ExceptionHandler(UnderAgeException.class)
        public ResponseEntity<ErrorResponse> handleUnderAge(
                        UnderAgeException exception,
                        HttpServletRequest request) {

                int statusCode = HttpStatus.BAD_REQUEST.value();
                ErrorResponse errorResponse = ErrorResponse.builder()
                                .message(exception.getMessage())
                                .statusCode(statusCode)
                                .path(request.getRequestURI())
                                .date(LocalDateTime.now())
                                .build();
                return ResponseEntity.badRequest().body(errorResponse);
        }

        @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
        public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupported(
                        HttpMediaTypeNotSupportedException exception,
                        HttpServletRequest request) {
                
                int statusCode = HttpStatus.UNSUPPORTED_MEDIA_TYPE.value();
                String contentMediaType = request.getContentType();

                ErrorResponse errorResponse = ErrorResponse.builder()
                                .message("Don't support %s content media type".formatted(contentMediaType))
                                .statusCode(statusCode)
                                .path(request.getRequestURI())
                                .date(LocalDateTime.now())
                                .build();
                return ResponseEntity.status(statusCode).body(errorResponse);
        }

        @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
        public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotAcceptable(
                HttpMediaTypeNotAcceptableException exception,
                        HttpServletRequest request) {
                int statusCode = HttpStatus.NOT_ACCEPTABLE.value();
                String acceptMediaType = request.getHeader("Accept");

                ErrorResponse errorResponse = ErrorResponse.builder()
                                .message("Don't support %s accept media type".formatted(acceptMediaType))
                                .statusCode(statusCode)
                                .path(request.getRequestURI())
                                .date(LocalDateTime.now())
                                .build();
                return ResponseEntity.status(statusCode).body(errorResponse);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ValidationErrorResponse> handleMethodArgumentNotValid(
                        MethodArgumentNotValidException exception,
                        HttpServletRequest request) {
                int statusCode = HttpStatus.UNPROCESSABLE_ENTITY.value();
                List<ValidationErrorField> fields = exception.getFieldErrors()
                                .stream()
                                .map(this::convertToMapField)
                                .toList();

                var errorResponse = ValidationErrorResponse.validationErrorResponseBuilder()
                                .message("Validation error")
                                .statusCode(statusCode)
                                .path(request.getRequestURI())
                                .date(LocalDateTime.now())
                                .fields(fields)
                                .build();

                return ResponseEntity.unprocessableEntity().body(errorResponse);
        }

        private ValidationErrorField convertToMapField(FieldError fieldError) {
                return ValidationErrorField.builder()
                        .field(fieldError.getField())
                        .message(fieldError.getDefaultMessage())
                        .build();
        }
}