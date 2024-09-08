package com.fernando.connected_minds_api.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import com.fernando.connected_minds_api.enums.UserGenre;
import com.fernando.connected_minds_api.exceptions.EntityAlreadyExistsException;
import com.fernando.connected_minds_api.exceptions.EntityNotFoundException;
import com.fernando.connected_minds_api.models.User;
import com.fernando.connected_minds_api.repositories.UserRepository;
import com.fernando.connected_minds_api.requests.LoginRequest;
import com.fernando.connected_minds_api.requests.RegisterRequest;
import com.fernando.connected_minds_api.responses.AuthResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.fernando.connected_minds_api.models.Avatar;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AuthServiceTest {
    @InjectMocks
    private AuthService authService;
    
    @Mock
    private UserRepository userRepository;

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AvatarsService avatarsService;

    @Mock
    private Authentication auth;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JWTService jwtService;


    @Test
    public void shouldAuthenticateUserAndReturnAResponseWithTokenRefreshTokenAndExpiresAt() {
        LoginRequest loginRequest = new LoginRequest("fernando@test.com", "fbarros123");
        User user = new User("fernandobarrosd", loginRequest.email(), loginRequest.password(),
        LocalDate.now(), "", "", UserGenre.MALE);
        Optional<String> expiresAt = Optional.of(LocalDateTime.now().plusMonths(1).toInstant(
            ZoneOffset.of("-03:00")).toString());

        user.setId(UUID.randomUUID());

        var authToken = new UsernamePasswordAuthenticationToken(
            loginRequest.email(),
            loginRequest.password()
        );

        when(applicationContext.getBean(AuthenticationManager.class)).thenReturn(authenticationManager);
        when(authenticationManager.authenticate(authToken)).thenReturn(auth);
        when(auth.getPrincipal()).thenReturn(user);
        when(jwtService.generateJWT(loginRequest.email())).thenReturn("jwt-token");
        when(jwtService.generateRefreshToken(user.getId())).thenReturn("jwt-refresh-token");
        when(jwtService.getExpiresAt("jwt-token")).thenReturn(expiresAt);


        AuthResponse authResponse = assertDoesNotThrow(() -> authService.authenticate(loginRequest));

        
        assertNotNull(authResponse);
        assertEquals("jwt-token", authResponse.token());
        assertEquals("jwt-refresh-token", authResponse.refreshToken());
        assertEquals(expiresAt.get(), authResponse.tokenExpiresAt());

    }

    @Test
    public void shouldNotAuthenticateUserAndThrowsException() {
        LoginRequest loginRequest = new LoginRequest("fernando@test.com", "fbarros123");
        var authToken = new UsernamePasswordAuthenticationToken(
            "fernando@test.com",
            "fbarros123"
        );
        
        when(applicationContext.getBean(AuthenticationManager.class)).thenReturn(authenticationManager);
        when(authenticationManager.authenticate(authToken)).thenThrow(BadCredentialsException.class);

        var exception = assertThrows(EntityNotFoundException.class, () -> authService.authenticate(loginRequest));
        
        assertEquals("User is not exists", exception.getMessage());
    }




    @Test
    public void shouldRegisterMaleUserAndReturnAAuthResponseWithTokenRefreshTokenAndExpiresAt() {
        RegisterRequest registerRequest = new RegisterRequest("fernandobarrosd", "fernando@test.com", "fbarros123",
        "", "", "MALE", LocalDate.now().toString());
        Optional<String> expiresAt = Optional.of(LocalDateTime.now().plusMonths(1).toInstant(
            ZoneOffset.of("-03:00")).toString());
        

        Avatar avatar = new Avatar();
        avatar.setUrl("male-avatar.jpg");

        User user = registerRequest.toEntity();

        
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encoded-password");
        when(avatarsService.getRandomMaleAvatar()).thenReturn(avatar);
        when(userRepository.save(user)).thenReturn(user);
        when(jwtService.generateJWT(user.getEmail())).thenReturn("jwt-token");
        when(jwtService.generateRefreshToken(user.getId())).thenReturn("jwt-refresh-token");
        when(jwtService.getExpiresAt("jwt-token")).thenReturn(expiresAt);


        AuthResponse authResponse = assertDoesNotThrow(() -> authService.registerUser(registerRequest));
        System.out.println(authResponse.token());
        System.out.println(authResponse.refreshToken());

        assertNotNull(authResponse);
        assertEquals("jwt-token", authResponse.token());
        assertEquals("jwt-refresh-token", authResponse.refreshToken());
        assertEquals(expiresAt.get(), authResponse.tokenExpiresAt());
    }

    @Test
    public void shouldNotRegisterUserAndThrowsEntityAlreadyExistsException() {
        RegisterRequest registerRequest = new RegisterRequest("fernandobarrosd", "fernando@test.com", "fbarros123",
        "", "", "MALE", LocalDate.now().toString());
        
        Avatar avatar = new Avatar();
        avatar.setUrl("male-avatar.jpg");

        User user = registerRequest.toEntity();

        when(passwordEncoder.encode(user.getPassword())).thenReturn("encoded-password");
        when(avatarsService.getRandomMaleAvatar()).thenReturn(avatar);
        when(userRepository.save(any(User.class))).thenThrow(IllegalArgumentException.class);

        var exception = assertThrows(EntityAlreadyExistsException.class, () -> authService.registerUser(registerRequest));


        assertEquals("User is already exists", exception.getMessage());
    }

}