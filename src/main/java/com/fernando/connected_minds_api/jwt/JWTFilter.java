package com.fernando.connected_minds_api.jwt;

import java.io.IOException;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.connected_minds_api.error.ErrorResponse;
import com.fernando.connected_minds_api.user.UserRepository;
import com.fernando.connected_minds_api.user.User;
import java.util.Optional;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

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
                var errorResponseBuilder = ErrorResponse.builder()
                    .path(request.getRequestURI());

                if (authorizationHeader != null) {
                    if (!authorizationHeader.startsWith("Bearer")) {
                        var errorResponse = errorResponseBuilder
                            .statusCode(HttpStatus.UNAUTHORIZED.value())
                            .date(LocalDateTime.now())
                            .message("Authorization header must be starts with Bearer")
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
                            .statusCode(HttpStatus.UNAUTHORIZED.value())
                            .date(LocalDateTime.now())
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
                                .statusCode(HttpStatus.UNAUTHORIZED.value())
                                .date(LocalDateTime.now())
                                .message("User is not found")
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
                }
                filterChain.doFilter(request, response);


            }

}