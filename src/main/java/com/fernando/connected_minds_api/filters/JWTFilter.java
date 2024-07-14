package com.fernando.connected_minds_api.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.connected_minds_api.models.User;
import com.fernando.connected_minds_api.repositories.UserRepository;
import com.fernando.connected_minds_api.responses.error.ErrorResponse;
import com.fernando.connected_minds_api.services.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
    private final JWTService jwtService;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null) {
            var errorResponseBuilder = ErrorResponse.builder()
                    .statusCode(HttpStatus.UNAUTHORIZED.value())
                    .date(LocalDateTime.now())
                    .path(request.getRequestURI());
            if (!authorizationHeader.startsWith("Bearer")) {
                var errorResponse = errorResponseBuilder
                        .message("JWT token is not starts with Bearer")
                                .build();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
                response.getWriter().flush();
                response.getWriter().close();
                return;
            }
            String token = authorizationHeader.replace("Bearer ", "");

            if (!jwtService.isValidToken(token)) {
                        var errorResponse = errorResponseBuilder
                        .message("JWT token is not valid")
                        .build();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
                response.getWriter().flush();
                response.getWriter().close();
                return;
            }

            String email = jwtService.getSubject(token).get();
            Optional<User> userOptional = userRepository.findByEmail(email);

            if (userOptional.isEmpty()) {
                var errorResponse = errorResponseBuilder
                        .message("User is not exists")
                        .build();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
                response.getWriter().flush();
                response.getWriter().close();
                return;
            }

            UserDetails user = userOptional.get();

            var usernamePasswordToken = new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    user.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(usernamePasswordToken);
            System.out.println(SecurityContextHolder.getContext().getAuthentication().isAuthenticated());

        }
        filterChain.doFilter(request, response);
    }
}

