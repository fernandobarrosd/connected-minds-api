package com.fernando.connected_minds_api.handlers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
import com.fernando.connected_minds_api.exceptions.CannotLikeTheOwnPostException;
import com.fernando.connected_minds_api.exceptions.EntityAlreadyExistsException;
import com.fernando.connected_minds_api.exceptions.EntityNotFoundException;
import com.fernando.connected_minds_api.exceptions.JWTTokenInvalidException;
import com.fernando.connected_minds_api.exceptions.UnderAgeException;
import com.fernando.connected_minds_api.exceptions.UserIsAlreadyExiststInCommunityOrGroupException;
import com.fernando.connected_minds_api.exceptions.UserIsNotOwnerOfResourceException;
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
        public ResponseEntity<ErrorResponse> handleUserIsAlreadyExiststInCommunityOrGroup(
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
                List<Map<String, String>> fields = exception.getFieldErrors()
                                .stream()
                                .map(this::convertToMapField)
                                .toList();

                var errorResponse = ValidationErrorResponse.validationErrorResponseBuilder()
                                .message("Validation failed")
                                .statusCode(statusCode)
                                .path(request.getRequestURI())
                                .date(LocalDateTime.now())
                                .fields(fields)
                                .build();

                return ResponseEntity.unprocessableEntity().body(errorResponse);
        }

        private Map<String, String> convertToMapField(FieldError fieldError) {
                return Map.of(
                                "field", fieldError.getField(),
                                "errorMessage", Objects.requireNonNull(fieldError.getDefaultMessage()));
        }
}