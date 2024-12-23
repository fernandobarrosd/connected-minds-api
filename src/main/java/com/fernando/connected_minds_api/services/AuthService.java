package com.fernando.connected_minds_api.services;

import com.fernando.connected_minds_api.exceptions.EntityAlreadyExistsException;
import com.fernando.connected_minds_api.exceptions.EntityNotFoundException;
import com.fernando.connected_minds_api.exceptions.JWTTokenInvalidException;
import com.fernando.connected_minds_api.exceptions.UnderAgeException;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
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
                loginRequest.password());
        Authentication auth;

        var authManager = applicationContext.getBean(AuthenticationManager.class);

        try {
            auth = authManager.authenticate(usernamePasswordToken);
        } catch (Exception exception) {
            throw new EntityNotFoundException("User is not exists");
        }
        User user = (User) auth.getPrincipal();
        String token = jwtService.generateJWT(user);
        String refreshToken = jwtService.generateRefreshToken(user.getId());
        String expiresAt = jwtService.getExpiresAt(token).get();

        return new AuthResponse(
                token,
                refreshToken,
                expiresAt,
                user.getId(),
                user.getUsername(),
                user.getPhotoURL(),
                user.getBannerURL(),
                user.getBio());
    }

    public AuthResponse generateNewToken(RefreshTokenRequest request) {
        String refreshToken = request.refreshToken();

        if (!jwtService.isValidToken(refreshToken)) {
            throw new JWTTokenInvalidException("Refresh token is not valid");
        }

        String userID = jwtService.getSubject(refreshToken).get();
        User user = userRepository.findById(UUID.fromString(userID))
                .orElseThrow(() -> new EntityNotFoundException("User is not exists"));

        String newToken = jwtService.generateJWT(user);
        String expiresAt = jwtService.getExpiresAt(newToken).get();

        return new AuthResponse(
                newToken,
                refreshToken,
                expiresAt,
                user.getId(),
                user.getUsername(),
                user.getPhotoURL(),
                user.getBannerURL(),
                user.getBio());

    }

    public AuthResponse registerUser(RegisterRequest request) {
        User user = request.toEntity();
        User userSaved = null;

        Integer currentYear = LocalDate.now().getYear();
        Integer birthYear = user.getBirthDate().getYear();
        Integer age = currentYear - birthYear;

        if (age < 13) {
            throw new UnderAgeException("User age must be greather than or equal to 13 years old");
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        try {
            userSaved = userRepository.save(user);
        } catch (IllegalArgumentException exception) {
            throw new EntityAlreadyExistsException("User is already exists");
        }

        String token = jwtService.generateJWT(user);
        String refreshToken = jwtService.generateRefreshToken(userSaved.getId());
        String expiresAt = jwtService.getExpiresAt(token).get();

        return new AuthResponse(
                token,
                refreshToken,
                expiresAt,
                userSaved.getId(),
                user.getUsername(),
                user.getPhotoURL(),
                user.getBannerURL(),
                user.getBio());
    }
}