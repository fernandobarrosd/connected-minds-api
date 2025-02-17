package com.fernando.connected_minds_api.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import org.springframework.context.ApplicationContext;
import com.fernando.connected_minds_api.exceptions.EntityAlreadyExistsException;
import com.fernando.connected_minds_api.exceptions.EntityNotFoundException;
import com.fernando.connected_minds_api.exceptions.UnderAgeException;
import com.fernando.connected_minds_api.jwt.JWTService;
import com.fernando.connected_minds_api.user.UserRepository;
import com.fernando.connected_minds_api.user.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
    private final JWTService jwtService;
    private final UserRepository userRepository;
    private final AuthenticationManager authManager;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("User is not found"));
    }

    public AuthResponse authenticate(LoginRequest loginRequest) {
        var usernamePasswordToken = new UsernamePasswordAuthenticationToken(
                loginRequest.email(),
                loginRequest.password());

        Authentication auth;

        try {
            auth = authManager.authenticate(usernamePasswordToken);
        } catch (Exception exception) {
            throw new EntityNotFoundException("User is not exists");
        }

        User user = (User) auth.getPrincipal();
        String token = jwtService.generateJWT(user);
        String refreshToken = jwtService.generateRefreshToken(user.getId());
        String tokenExpiresAt = jwtService.getExpiresAt(token).get();

        return AuthResponse.builder()
            .token(token)
            .refreshToken(refreshToken)
            .tokenExpiresAt(tokenExpiresAt)
            .userId(user.getId())
            .username(user.getUsername())
            .photoURL(user.getPhotoURL())
            .bannerURL(user.getBannerURL())
            .bio(user.getBio())
            .build();
    }

    public AuthResponse register(RegisterRequest registerRequest) {
        User user = registerRequest.toEntity();

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
        String tokenExpiresAt = jwtService.getExpiresAt(token).get();

        return AuthResponse.builder()
            .token(token)
            .refreshToken(refreshToken)
            .tokenExpiresAt(tokenExpiresAt)
            .userId(user.getId())
            .username(user.getUsername())
            .photoURL(user.getPhotoURL())
            .bannerURL(user.getBannerURL())
            .bio(user.getBio())
            .build();
    }
}