package com.fernando.connected_minds_api.services;

import com.fernando.connected_minds_api.enums.UserGenre;
import com.fernando.connected_minds_api.exceptions.EntityAlreadyExistsException;
import com.fernando.connected_minds_api.exceptions.EntityNotFoundException;
import com.fernando.connected_minds_api.exceptions.JWTTokenInvalidException;
import com.fernando.connected_minds_api.models.Avatar;
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
    private final AvatarsService avatarsService;
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
        String refreshToken = jwtService.generateRefreshToken(user.getId());
        String expiresAt = jwtService.getExpiresAt(token).get();

        return new AuthResponse(token, refreshToken, expiresAt);
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
        String expiresAt = jwtService.getExpiresAt(newToken).get();

        return new AuthResponse(newToken, refreshToken, expiresAt);

    }

    public AuthResponse registerUser(RegisterRequest request) {
        User user = request.toEntity();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        if (request.photoURL() == null || request.photoURL().isBlank()) {
            Avatar avatar;

            if (user.getGenre() == UserGenre.MALE) {
                avatar = avatarsService.getRandomMaleAvatar();
            }
            else {
                avatar = avatarsService.getRandomFemaleAvatar();
            }

            user.setPhotoURL(avatar.getUrl());
        }

        try {
            userRepository.save(user);
        }
        catch (IllegalArgumentException exception) {
            throw new EntityAlreadyExistsException("User is already exists");
        }
        String token = jwtService.generateJWT(user.getEmail());
        String refreshToken = jwtService.generateRefreshToken(user.getId());
        String expiresAt = jwtService.getExpiresAt(token).get();

        return new AuthResponse(token, refreshToken, expiresAt);

    }
}