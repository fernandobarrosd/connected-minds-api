package com.fernando.connected_minds_api.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import com.fernando.connected_minds_api.enums.UserGenre;
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
import com.fernando.connected_minds_api.utils.FakerUtils;

import net.datafaker.Faker;

import org.springframework.security.crypto.password.PasswordEncoder;


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
    private Authentication auth;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JWTService jwtService;

    private Faker faker;

    @BeforeEach
    public void setup() {
        this.faker = new Faker();
    }

    @Test
    public void shouldAuthenticateUserAndReturnAResponseWithTokenRefreshTokenAndExpiresAt() {
        LoginRequest loginRequest = FakerUtils.generateFakeLoginRequest(faker);
        String ownerUsername = faker.regexify("username_[A-Za-z]{4,8}");
        UserGenre genre = faker.options().option(UserGenre.class);

        LocalDate birthDate = LocalDate.of(1980, 10, 5);

        User user = new User(ownerUsername, loginRequest.email(), loginRequest.password(),
        birthDate, null, null, genre);

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
        when(jwtService.generateJWT(user)).thenReturn("jwt-token");
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
        LoginRequest loginRequest = FakerUtils.generateFakeLoginRequest(faker);
        var authToken = new UsernamePasswordAuthenticationToken(
            loginRequest.email(),
            loginRequest.password()
        );
        
        when(applicationContext.getBean(AuthenticationManager.class)).thenReturn(authenticationManager);
        when(authenticationManager.authenticate(authToken)).thenThrow(BadCredentialsException.class);

        var exception = assertThrows(EntityNotFoundException.class, () -> authService.authenticate(loginRequest));
        
        assertNotNull(exception);
        assertEquals("User is not exists", exception.getMessage());
    }


    @Test
    public void shouldRegisterUserAndReturnAAuthResponseWithTokenRefreshTokenAndExpiresAt() {
        LocalDate birthDate = LocalDate.of(1980, 10, 5);
        RegisterRequest registerRequest = FakerUtils.generateFakeRegisterRequest(faker);
        Optional<String> expiresAt = Optional.of(LocalDateTime.now().plusMonths(1).toInstant(
            ZoneOffset.of("-03:00")).toString());
        
        User user = registerRequest.toEntity();
        System.out.println(user.getEmail());

        
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encoded-password");
        when(userRepository.save(user)).thenReturn(user);
        when(jwtService.generateJWT(user)).thenReturn("jwt-token");
        when(jwtService.generateRefreshToken(user.getId())).thenReturn("jwt-refresh-token");
        when(jwtService.getExpiresAt("jwt-token")).thenReturn(expiresAt);


        AuthResponse authResponse = assertDoesNotThrow(() -> authService.registerUser(registerRequest));

        assertEquals(registerRequest.username(), "fernandobarrosd");
        assertEquals(registerRequest.email(), "fernando@test.com");
        assertEquals(registerRequest.password(), "fbarros123");
        assertEquals(registerRequest.genre(), "MALE");
        assertEquals(registerRequest.birthDate(), "1980-10-05");
        assertNull(registerRequest.photoURL());
        assertNull(registerRequest.bannerURL());
        assertNotNull(authResponse);
        assertEquals("jwt-token", authResponse.token());
        assertEquals("jwt-refresh-token", authResponse.refreshToken());
        assertEquals(expiresAt.get(), authResponse.tokenExpiresAt());

    }

    @Test
    public void shouldNotRegisterUserAndThrowsEntityAlreadyExistsException() {
        LocalDate birthDate = LocalDate.of(1980, 10, 5);
        RegisterRequest registerRequest = new RegisterRequest("fernandobarrosd", "fernando@test.com", "fbarros123",
        null, null, "MALE", birthDate.toString());

        User user = registerRequest.toEntity();

        when(passwordEncoder.encode(user.getPassword())).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenThrow(IllegalArgumentException.class);

        var exception = assertThrows(EntityAlreadyExistsException.class, () -> authService.registerUser(registerRequest));


        assertEquals("User is already exists", exception.getMessage());
    }

    @Test
    public void shouldNotRegisterUserWithAgeBellow13() {
        LocalDate dateNow = LocalDate.now();

        RegisterRequest registerRequest = new RegisterRequest("fernandobarrosd", 
        "fernando@test.com", "fbarros123",
        null, null, "MALE", dateNow.toString());

        var exception = assertThrows(UnderAgeException.class, () -> authService.registerUser(registerRequest));

        assertNotNull(exception);
        assertNotNull(exception.getMessage());
        assertEquals("User age must be greather than or equal to 13 years old", 
        exception.getMessage());

    }

    @Test
    public void shouldGenerateNewToken() {
        var refreshTokenRequest = new RefreshTokenRequest(
            "refresh-token"
        );
        LocalDate birthDate = LocalDate.of(1980, 10, 5);

        LocalDateTime currentTime = LocalDateTime.now();

        String refreshToken = refreshTokenRequest.refreshToken();

        UUID userID = UUID.randomUUID();

        User user = new User("fernandobarrosd", "fernando@test.com", 
        "fbarros123",
        birthDate, null, null, UserGenre.MALE);

        when(jwtService.isValidToken(refreshToken)).thenReturn(true);
        when(jwtService.getSubject(refreshToken)).thenReturn(Optional.of(userID.toString()));
        when(userRepository.findById(userID)).thenReturn(Optional.of(user));
        when(jwtService.generateJWT(user)).thenReturn("new-token");
        when(jwtService.getExpiresAt("new-token")).thenReturn(
            Optional.of(currentTime.toString())
        );

        AuthResponse authResponse = authService.generateNewToken(refreshTokenRequest);

        assertNotNull(authResponse);
        assertNotNull(authResponse.token());
        assertNotNull(authResponse.refreshToken());
        assertNotNull(authResponse.tokenExpiresAt());

        assertEquals("new-token", authResponse.token());
        assertEquals(refreshToken, authResponse.refreshToken());
        assertEquals(currentTime.toString(), authResponse.tokenExpiresAt());
    }


    @Test
    public void shouldNotGenerateNewToken() {
        var refreshTokenRequest = new RefreshTokenRequest(
            "refresh-token"
        );

        var refreshToken = refreshTokenRequest.refreshToken();

        when(jwtService.isValidToken(refreshToken)).thenReturn(false);

        var exception = assertThrows(JWTTokenInvalidException.class, 
        () -> authService.generateNewToken(refreshTokenRequest));

        assertNotNull(exception);
        assertNotNull(exception.getMessage());
        assertEquals("Refresh token is not valid", exception.getMessage());
    }

}