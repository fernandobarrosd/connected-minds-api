package com.fernando.connected_minds_api.services;

import com.fernando.connected_minds_api.exceptions.EntityAlreadyExistsException;
import com.fernando.connected_minds_api.exceptions.EntityNotFoundException;
import com.fernando.connected_minds_api.exceptions.JWTTokenInvalidException;
import com.fernando.connected_minds_api.models.User;
import com.fernando.connected_minds_api.repositories.UserRepository;
import com.fernando.connected_minds_api.requests.LoginRequest;
import com.fernando.connected_minds_api.requests.RefreshTokenRequest;
import com.fernando.connected_minds_api.requests.RegisterRequest;
import com.fernando.connected_minds_api.responses.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
    private final JWTService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ApplicationContext applicationContext;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("User is not exists"));
    }

    public AuthResponse authenticate(LoginRequest loginRequest) {
        var usernamePasswordToken = new UsernamePasswordAuthenticationToken(
                loginRequest.email(),
                loginRequest.password()
        );
        Authentication auth;

        var authManager = applicationContext.getBean(AuthenticationManager.class);

        try {
            auth = authManager.authenticate(usernamePasswordToken);
        }
        catch (AuthenticationException exception) {
            throw new EntityNotFoundException("User is not exists");
        }
        User user = (User) auth.getPrincipal();
        String token = jwtService.generateJWT(loginRequest.email());
        String refreshToken = jwtService.generateRefreshToken(user.getId().toString());

        return new AuthResponse(token, refreshToken);
    }

    public AuthResponse generateNewToken(RefreshTokenRequest request) {
        String refreshToken = request.refreshToken();

        if (!jwtService.isValidToken(refreshToken)) {
            throw new JWTTokenInvalidException("Refresh token is not valid");
        }

        String userID = jwtService.getSubject(refreshToken).get();
        User user = userRepository.findById(UUID.fromString(userID))
                .orElseThrow(() -> new EntityNotFoundException("User is not exists"));

        String newToken = jwtService.generateJWT(user.getEmail());

        return new AuthResponse(newToken, refreshToken);

    }

    public AuthResponse registerUser(RegisterRequest request) {
        User user = request.toEntity();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        try {
            userRepository.save(user);
        }
        catch (Exception exception) {
            throw new EntityAlreadyExistsException("User is already exists");
        }
        return authenticate(new LoginRequest(request.email(), request.password()));
    }
}