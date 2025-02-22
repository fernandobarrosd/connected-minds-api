package com.fernando.connected_minds_api.auth;

import com.fernando.connected_minds_api.auth.factories.LoginRequestFactory;
import com.fernando.connected_minds_api.auth.factories.RegisterRequestFactory;
import com.fernando.connected_minds_api.avatar.AvatarService;
import com.fernando.connected_minds_api.exceptions.EntityAlreadyExistsException;
import com.fernando.connected_minds_api.exceptions.EntityNotFoundException;
import com.fernando.connected_minds_api.exceptions.UnderAgeException;
import com.fernando.connected_minds_api.formatters.DateTimeFormatters;
import com.fernando.connected_minds_api.jwt.JWTService;
import com.fernando.connected_minds_api.singleton.FakerSingleton;
import com.fernando.connected_minds_api.user.User;
import com.fernando.connected_minds_api.user.UserFactory;
import com.fernando.connected_minds_api.user.UserRepository;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;


@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @InjectMocks
    private AuthService authService;

    @Mock
    private JWTService jwtService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AvatarService avatarService;

    private LocalDate localDateNow;

    private Faker faker;

    @BeforeEach
    public void setup() {
        faker = FakerSingleton.getInstance();
        localDateNow = LocalDate.parse("20-12-2025", DateTimeFormatters.LOCAL_DATE_FORMATTER);
    }

    @Test
    public void shouldAuthenticateUserAndReturnAuthResponse() {
        LoginRequest loginRequest = LoginRequestFactory.createLoginRequest(faker);
        User user = UserFactory.createUser(faker, loginRequest);
        String token = "token";
        String refreshToken = "refresh-token";
        String tokenExpiresAt = "token-expire-at";

        user.setEmail(loginRequest.email());
        user.setPassword("encoded-password");

        Authentication userAuthentication = mock(Authentication.class);
        AuthenticationManager authManager = mock(AuthenticationManager.class);



        when(applicationContext.getBean(AuthenticationManager.class))
                .thenReturn(authManager);

        when(userAuthentication.getPrincipal()).thenReturn(user);
        when(jwtService.generateJWT(any(User.class))).thenReturn(token);
        when(jwtService.generateRefreshToken(any(UUID.class))).thenReturn(refreshToken);
        when(jwtService.getExpiresAt(token))
                .thenReturn(Optional.of(tokenExpiresAt));
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(userAuthentication);

        AuthResponse authResponse = authService.authenticate(loginRequest);

        assertEquals(user.getId(), authResponse.userId());
        assertEquals(user.getUsername(), authResponse.username());
        assertEquals(user.getBio(), authResponse.bio());
        assertEquals(user.getPhotoURL(), authResponse.photoURL());
        assertEquals(user.getGenre(), authResponse.userGenre());
        assertEquals(token, authResponse.token());
        assertEquals(refreshToken, authResponse.refreshToken());
        assertEquals(tokenExpiresAt, authResponse.tokenExpiresAt());
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionIfUserIsNotFounded() {
        LoginRequest loginRequest = LoginRequestFactory.createLoginRequest(faker);
        AuthenticationManager authManager = mock(AuthenticationManager.class);

        when(applicationContext.getBean(AuthenticationManager.class))
                .thenReturn(authManager);

        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(RuntimeException.class);

        var exception = assertThrows(
                EntityNotFoundException.class,
                () -> authService.authenticate(loginRequest)
        );

        assertEquals("User is not found", exception.getMessage());


    }

    @Test
    public void shouldRegisterUserAndReturnAuthResponse() {
        RegisterRequest registerRequest = RegisterRequestFactory.createRegisterRequest(faker);
        User user = registerRequest.toEntity();
        user.setPhotoURL("fake-url/avatars/avatar.png");

        String token = "token";
        String refreshToken = "refresh-token";
        String tokenExpiresAt = "token-expire-at";

        try(MockedStatic<LocalDate> localDateMock = mockStatic(LocalDate.class, CALLS_REAL_METHODS)) {
            localDateMock.when(LocalDate::now).thenReturn(localDateNow);

            when(passwordEncoder.encode(any(String.class)))
                    .thenReturn("encoded-password");

            when(avatarService.generateAvatarFromUserGenre(any()))
                    .thenReturn("avatar.png");

            when(userRepository.save(any(User.class))).thenReturn(user);

            when(jwtService.generateJWT(any())).thenReturn(token);
            when(jwtService.generateRefreshToken(any()))
                    .thenReturn(refreshToken);
            when(jwtService.getExpiresAt(token))
                    .thenReturn(Optional.of(tokenExpiresAt));

            AuthResponse authResponse = authService.register(registerRequest, "fake-url");

            assertEquals(user.getId(), authResponse.userId());
            assertEquals(user.getUsername(), authResponse.username());
            assertEquals(user.getBio(), authResponse.bio());
            assertEquals(user.getPhotoURL(), authResponse.photoURL());
            assertEquals(user.getGenre(), authResponse.userGenre());
            assertEquals(token, authResponse.token());
            assertEquals(refreshToken, authResponse.refreshToken());
            assertEquals(tokenExpiresAt, authResponse.tokenExpiresAt());

        }
    }

    @Test
    public void shouldThrowEntityAlreadyExistsExceptionIfUserIsAlreadyExists() {
        RegisterRequest registerRequest = RegisterRequestFactory.createRegisterRequest(faker);

        when(userRepository.existsByEmail(any(String.class)))
                .thenReturn(true);

        var exception = assertThrows(
                EntityAlreadyExistsException.class,
                () -> authService.register(registerRequest, "fake-url")
        );

        assertEquals("User is already exists", exception.getMessage());


    }

    @Test
    public void shouldThrowUnderAgeExceptionIfUserAgeIsLessThan13() {
        RegisterRequest registerRequest = RegisterRequestFactory
                .createRegisterRequestWithAgeLessThan13(faker);

        try(MockedStatic<LocalDate> localDateMock = mockStatic(LocalDate.class, CALLS_REAL_METHODS)) {
            localDateMock.when(LocalDate::now).thenReturn(localDateNow);

            var exception = assertThrows(
                    UnderAgeException.class,
                    () -> authService.register(registerRequest, "fake-url")
            );

            assertEquals(
                    "User age must be greater than or equal that 13 years old",
                    exception.getMessage()
            );


        }
    }
}