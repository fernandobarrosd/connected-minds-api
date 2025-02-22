package com.fernando.connected_minds_api.auth;

import com.fernando.connected_minds_api.avatar.AvatarService;
import org.springframework.beans.factory.annotation.Value;
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
    private final ApplicationContext applicationContext;
    private final PasswordEncoder passwordEncoder;
    private final AvatarService avatarService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("User is not found"));
    }

    public AuthResponse authenticate(LoginRequest loginRequest) {
        AuthenticationManager authManager = applicationContext.getBean(AuthenticationManager.class);

        var usernamePasswordToken = new UsernamePasswordAuthenticationToken(
                loginRequest.email(),
                loginRequest.password()
        );

        try {
            Authentication auth = authManager.authenticate(usernamePasswordToken);

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
                    .bio(user.getBio())
                    .userGenre(user.getGenre())
                    .build();
        } catch (RuntimeException exception) {
            throw new EntityNotFoundException("User is not found");
        }
    }

    public AuthResponse register(RegisterRequest registerRequest, String apiURL) {
        User user = registerRequest.toEntity();
        boolean userIsExists = userRepository.existsByEmail(registerRequest.email());

        if (userIsExists) {
            throw new EntityAlreadyExistsException("User is already exists");
        }

        int currentYear = LocalDate.now().getYear();
        int birthYear = user.getBirthDate().getYear();
        int age = currentYear - birthYear;

        if (age < 13) {
            throw new UnderAgeException("User age must be greater than or equal that 13 years old");
        }
        String avatar = avatarService.generateAvatarFromUserGenre(user.getGenre());
        String photoURL = "%s/avatars/%s".formatted(apiURL, avatar);
        user.setPhotoURL(photoURL);

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        User userSaved = userRepository.save(user);

        String token = jwtService.generateJWT(userSaved);
        String refreshToken = jwtService.generateRefreshToken(userSaved.getId());
        String tokenExpiresAt = jwtService.getExpiresAt(token).get();

        return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .tokenExpiresAt(tokenExpiresAt)
                .userId(userSaved.getId())
                .username(userSaved.getUsername())
                .photoURL(user.getPhotoURL())
                .bio(userSaved.getBio())
                .userGenre(userSaved.getGenre())
                .build();
    }
}