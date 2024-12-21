package com.fernando.connected_minds_api.handlers;

import java.io.IOException;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import com.fernando.connected_minds_api.responses.error.ErrorResponse;

@Component
@RequiredArgsConstructor
public class UnauthorizedHandler implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
         ErrorResponse responseError = ErrorResponse.builder()
        .message("This resource require Authentication header JWT token")
        .date(LocalDateTime.now())
        .statusCode(HttpStatus.UNAUTHORIZED.value())
        .path(request.getRequestURI())
        .build();

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(responseError));
    }
}