package com.fernando.connected_minds_api.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import com.fernando.connected_minds_api.enums.UserGenre;
import com.fernando.connected_minds_api.exceptions.EntityNotFoundException;
import com.fernando.connected_minds_api.exceptions.EntityAlreadyExistsException;
import com.fernando.connected_minds_api.models.User;
import com.fernando.connected_minds_api.repositories.UserRepository;
import com.fernando.connected_minds_api.requests.LoginRequest;
import com.fernando.connected_minds_api.requests.RegisterRequest;
import com.fernando.connected_minds_api.responses.AuthResponse;
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
        private AuthenticationManager authenticationManager;

        @Mock
        private JWTService jwtService;

        private LocalDate localDate;

        private LoginRequest loginRequest;

        private RegisterRequest registerRequest;

        @BeforeEach
        public void setup() {
                this.loginRequest = new LoginRequest(
                                "email@email.com",
                                "password");
                this.registerRequest = new RegisterRequest(
                                "username",
                                "email@email.com",
                                "password123",
                                null,
                                null,
                                null,
                                "MALE",
                                "2004-12-20");
                this.localDate = LocalDate.of(2024, 12, 20);
        }

        @Test
        public void shouldExecuteAuthenticateUserMethodAndReturnAuthResponse() {
                LocalDate birthDate = LocalDate.of(2004, 12, 20);
                Optional<String> jwtExpiresAt = Optional.of(
                                LocalDateTime.of(
                                                2024, 12, 20, 19, 25).toString());
                String jwtToken = "jwt-token";
                String refreshToken = "jwt-refresh-token";
                UUID userID = UUID.fromString("d76ea812-8dd5-4d7e-848a-8d3f4c103742");

                Authentication auth = mock(Authentication.class);

                User user = new User(
                                userID,
                                "username",
                                loginRequest.email(),
                                loginRequest.password(),
                                birthDate,
                                null,
                                null,
                                null,
                                UserGenre.MALE);

                when(applicationContext.getBean(AuthenticationManager.class))
                                .thenReturn(authenticationManager);

                when(auth.getPrincipal()).thenReturn(user);

                when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                                .thenReturn(auth);

                when(jwtService.generateJWT(user)).thenReturn(jwtToken);

                when(jwtService.generateRefreshToken(user.getId()))
                                .thenReturn(refreshToken);

                when(jwtService.getExpiresAt(jwtToken)).thenReturn(jwtExpiresAt);

                AuthResponse authResponse = authService.authenticate(loginRequest);

                assertNotNull(authResponse.id());
                assertNotNull(authResponse.username());
                assertNotNull(authResponse.token());
                assertNotNull(authResponse.refreshToken());
                assertNotNull(authResponse.tokenExpiresAt());

                assertNull(authResponse.bio());
                assertNull(authResponse.photoURL());
                assertNull(authResponse.bannerURL());

                assertEquals(user.getId(), authResponse.id());
                assertEquals(user.getUsername(), authResponse.username());
                assertEquals(jwtToken, authResponse.token());
                assertEquals(refreshToken, authResponse.refreshToken());
                assertEquals(jwtExpiresAt.get(), authResponse.tokenExpiresAt());
        }

        @Test
        public void shouldNotAuthenticateUserAndThrowsException() {
                String exceptionMessage = "User is not exists";

                when(applicationContext.getBean(AuthenticationManager.class))
                                .thenReturn(authenticationManager);

                when(authenticationManager.authenticate(
                                any(UsernamePasswordAuthenticationToken.class)))
                                .thenThrow(BadCredentialsException.class);

                var exception = assertThrows(EntityNotFoundException.class, () -> {
                        authService.authenticate(loginRequest);
                });

                assertNotNull(exception.getMessage());

                assertEquals(exceptionMessage, exception.getMessage());

        }

        @Test
        public void shouldExecuteRegisterUserAndReturnAuthResponse() {
                try (MockedStatic<LocalDate> localDateMock = mockStatic(LocalDate.class,
                                CALLS_REAL_METHODS)) {
                        LocalDate birthDate = LocalDate.of(2004, 12, 20);
                        LocalDate localDate = LocalDate.of(2024, 12, 20);
                        Optional<String> jwtExpiresAt = Optional.of(
                                        LocalDateTime.of(
                                                        2024, 12, 20, 19, 25).toString());
                        String jwtToken = "jwt-token";
                        String refreshToken = "jwt-refresh-token";
                        UUID userID = UUID.fromString("d76ea812-8dd5-4d7e-848a-8d3f4c103742");

                        User user = new User(
                                        userID,
                                        "username",
                                        loginRequest.email(),
                                        loginRequest.password(),
                                        birthDate,
                                        null,
                                        null,
                                        null,
                                        UserGenre.MALE);

                        localDateMock.when(() -> LocalDate.now()).thenReturn(localDate);

                        when(passwordEncoder.encode(any(String.class)))
                                        .thenReturn("password123-encoded");

                        when(userRepository.save(any(User.class))).thenReturn(user);

                        when(jwtService.generateJWT(any())).thenReturn(jwtToken);

                        when(jwtService.generateRefreshToken(any(UUID.class))).thenReturn(refreshToken);

                        when(jwtService.getExpiresAt(any(String.class))).thenReturn(jwtExpiresAt);

                        AuthResponse authResponse = authService.registerUser(registerRequest);

                        assertNotNull(authResponse.id());
                        assertNotNull(authResponse.username());
                        assertNotNull(authResponse.token());
                        assertNotNull(authResponse.refreshToken());
                        assertNotNull(authResponse.tokenExpiresAt());

                        assertNull(authResponse.bio());
                        assertNull(authResponse.photoURL());
                        assertNull(authResponse.bannerURL());

                        assertEquals(user.getId(), authResponse.id());
                        assertEquals(user.getUsername(), authResponse.username());
                        assertEquals(jwtToken, authResponse.token());
                        assertEquals(refreshToken, authResponse.refreshToken());
                        assertEquals(jwtExpiresAt.get(), authResponse.tokenExpiresAt());

                }
        }

        @Test
        public void shouldNotRegisterUserAndThrowsException() {
                LocalDate birthDate = LocalDate.of(2004, 12, 20);
                
                String exceptionMessage = "User is already exists";

                try (MockedStatic<LocalDate> localDateMock = mockStatic(LocalDate.class)) {
                        localDateMock.when(() -> LocalDate.now()).thenReturn(localDate);
                        localDateMock.when(() -> LocalDate.parse(registerRequest.birthDate())).thenReturn(birthDate);
                        
                        when(passwordEncoder.encode(any(String.class)))
                                        .thenReturn("password123-encoded");

                        when(userRepository.save(any(User.class))).thenThrow(IllegalArgumentException.class);

                        var exception = assertThrows(EntityAlreadyExistsException.class,
                                        () -> authService.registerUser(registerRequest));

                        assertNotNull(exception.getMessage());

                        assertEquals(exceptionMessage, exception.getMessage());

                }
        }

        @Test
        public void shouldNotRegisterUserWithAgeBellow13() {

        }

        @Test
        public void shouldGenerateNewToken() {

        }

        @Test
        public void shouldNotGenerateNewToken() {

        }

}