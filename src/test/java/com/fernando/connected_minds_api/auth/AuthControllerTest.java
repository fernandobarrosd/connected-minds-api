package com.fernando.connected_minds_api.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.connected_minds_api.auth.factories.LoginRequestFactory;
import com.fernando.connected_minds_api.auth.factories.RegisterRequestFactory;
import com.fernando.connected_minds_api.error.ErrorResponse;
import com.fernando.connected_minds_api.error.ValidationErrorField;
import com.fernando.connected_minds_api.error.ValidationErrorResponse;
import com.fernando.connected_minds_api.formatters.DateTimeFormatters;
import com.fernando.connected_minds_api.jwt.JWTService;
import com.fernando.connected_minds_api.singleton.FakerSingleton;
import com.fernando.connected_minds_api.user.UserRepository;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JWTService jwtService;

    @Autowired
    private UserRepository userRepository;

    private LocalDateTime localDateTimeNow;

    private Faker faker;

    @Nested
    public class AuthLoginTests {
        @BeforeEach
        public void setup() {
            localDateTimeNow = LocalDateTime.parse("20-12-2025 00:00:00", DateTimeFormatters.LOCAL_DATE_TIME_FORMATTER);
            faker = FakerSingleton.getInstance();
        }

        @AfterEach
        public void after() {
            userRepository.deleteAll();
        }

        @Test
        public void shouldAuthenticateUserWithSuccessAndReturnResponseWithStatus200Ok() throws Exception {
            RegisterRequest registerRequest = RegisterRequestFactory.createRegisterRequest(faker);
            LoginRequest loginRequest = LoginRequest.builder()
                    .email(registerRequest.email())
                    .password(registerRequest.password())
                    .build();

            when(jwtService.generateJWT(any())).thenReturn("token");
            when(jwtService.generateRefreshToken(any())).thenReturn("refresh-token");
            when(jwtService.getExpiresAt(any())).thenReturn(Optional.of("token-expires-at"));

            MvcResult registerUserMocvMvcResult = mockMvc.perform(
                            post("/auth/register")
                                    .contentType("application/json")
                                    .accept("application/json")
                                    .content(objectMapper.writeValueAsString(registerRequest)))
                    .andReturn();

            AuthResponse expectedResponse = objectMapper.readValue(
                    registerUserMocvMvcResult.getResponse().getContentAsString(),
                    AuthResponse.class
            );

            mockMvc.perform(
                            post("/auth/login")
                                    .contentType("application/json")
                                    .accept("application/json")
                                    .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)))
                    .andDo(print());

        }

        @Test
        public void shouldNotAuthenticateUserIfUserIsNotExistsAndReturnErrorResponseWithStatus404NotFound() throws Exception {
            try(MockedStatic<LocalDateTime> localDateTimeMock = mockStatic(LocalDateTime.class)) {
                localDateTimeMock.when(LocalDateTime::now).thenReturn(localDateTimeNow);
                LoginRequest loginRequest = LoginRequestFactory.createLoginRequest(faker);
                ErrorResponse errorResponse = ErrorResponse.builder()
                        .message("User is not found")
                        .path("/auth/login")
                        .date(LocalDateTime.now())
                        .statusCode(404)
                        .build();

                mockMvc.perform(
                                post("/auth/login")
                                        .contentType("application/json")
                                        .accept("application/json")
                                        .content(objectMapper.writeValueAsString(loginRequest)))
                        .andExpect(status().isNotFound())
                        .andExpect(content().json(objectMapper.writeValueAsString(errorResponse)))
                        .andDo(print());
            }
        }

        @Test
        public void shouldNotAuthenticateUserIfLoginRequestHaveNullFieldsWithStatus422UnprocessableEntity() throws Exception {
            LoginRequest loginRequest = LoginRequest.builder().build();

            try(MockedStatic<LocalDateTime> localDateTimeMock = mockStatic(LocalDateTime.class)) {
                localDateTimeMock.when(LocalDateTime::now).thenReturn(localDateTimeNow);

                ValidationErrorField  emailFieldIsRequiredErrorField = ValidationErrorField.builder()
                        .field("email")
                        .message("email field is required")
                        .build();

                ValidationErrorField  passwordFieldIsRequired = ValidationErrorField.builder()
                        .field("password")
                        .message("password field is required")
                        .build();

                ValidationErrorField  passwordFieldNotShouldBeEmptyErrorField = ValidationErrorField.builder()
                        .field("password")
                        .message("password field not should be empty")
                        .build();

                List<ValidationErrorField> validationErrorFields = List.of(
                        emailFieldIsRequiredErrorField,
                        passwordFieldIsRequired,
                        passwordFieldNotShouldBeEmptyErrorField
                );

                var validationErrorResponse = ValidationErrorResponse.validationErrorResponseBuilder()
                        .message("Validation error")
                        .path("/auth/login")
                        .statusCode(422)
                        .date(LocalDateTime.now())
                        .fields(validationErrorFields)
                        .build();

                mockMvc.perform(
                        post("/auth/login")
                                .contentType("application/json")
                                .accept("application/json")
                                .content(objectMapper.writeValueAsString(loginRequest)))
                        .andExpect(status().isUnprocessableEntity())
                        .andExpect(content().json(objectMapper.writeValueAsString(validationErrorResponse)))
                        .andDo(print());
            }

        }

        @Test
        public void shouldNotAuthenticateUserWithInvalidEmailAndReturnErrorResponseWithStatus422UnprocessableEntity() throws Exception {
            LoginRequest loginRequest = LoginRequestFactory.createLoginRequestWithInvalidEmail(faker);

            var invalidEmailErrorField = ValidationErrorField.builder()
                    .field("email")
                    .message("E-mail is not valid")
                    .build();

            try(MockedStatic<LocalDateTime> localDateTimeMock = mockStatic(LocalDateTime.class)) {
                localDateTimeMock.when(LocalDateTime::now).thenReturn(localDateTimeNow);

                var validationErrorResponse =  ValidationErrorResponse.validationErrorResponseBuilder()
                        .message("Validation error")
                        .path("/auth/login")
                        .statusCode(422)
                        .date(LocalDateTime.now())
                        .fields(List.of(invalidEmailErrorField))
                        .build();

                mockMvc.perform(
                        post("/auth/login")
                                .contentType("application/json")
                                .accept("application/json")
                                .content(objectMapper.writeValueAsString(loginRequest)))
                        .andExpect(status().isUnprocessableEntity())
                        .andExpect(content().json(objectMapper.writeValueAsString(validationErrorResponse)))
                        .andDo(print());
            }
        }

        @Test
        public void shouldNotAuthenticateUserIfPasswordHaveLessThat6CharactersAndReturnErrorResponseWithStatus422UnprocessableEntity() throws Exception {
            LoginRequest loginRequest = LoginRequestFactory.createLoginRequestWithInvalidPassword(faker);

            try(MockedStatic<LocalDateTime> localDateTimeMock = mockStatic(LocalDateTime.class)) {
                localDateTimeMock.when(LocalDateTime::now).thenReturn(localDateTimeNow);

                var invalidPasswordErrorField = ValidationErrorField.builder()
                        .field("password")
                        .message("password field should be min 6 characters")
                        .build();

                var validationErrorResponse = ValidationErrorResponse.validationErrorResponseBuilder()
                        .message("Validation error")
                        .path("/auth/login")
                        .statusCode(422)
                        .date(LocalDateTime.now())
                        .fields(List.of(invalidPasswordErrorField))
                        .build();

                mockMvc.perform(
                        post("/auth/login")
                                .contentType("application/json")
                                .accept("application/json")
                                .content(objectMapper.writeValueAsString(loginRequest)))
                        .andExpect(status().isUnprocessableEntity())
                        .andExpect(content().json(objectMapper.writeValueAsString(validationErrorResponse)))
                        .andDo(print());
            }
        }
    }

    @Nested
    public class AuthRegisterTests {
        @BeforeEach
        public void setup() {
            localDateTimeNow = LocalDateTime.parse("20-12-2025 00:00:00", DateTimeFormatters.LOCAL_DATE_TIME_FORMATTER);
            faker = FakerSingleton.getInstance();
        }

        @AfterEach
        public void after() {
            userRepository.deleteAll();
        }

        @Test
        public void shouldRegisterUserAndReturnResponseWithStatus201Created() throws Exception {
            RegisterRequest registerRequest = RegisterRequestFactory.createRegisterRequest(faker);
            LoginRequest loginRequest = LoginRequest.builder()
                    .email(registerRequest.email())
                    .password(registerRequest.password())
                    .build();

            when(jwtService.generateJWT(any())).thenReturn("token");
            when(jwtService.generateRefreshToken(any())).thenReturn("refresh-token");
            when(jwtService.getExpiresAt(any())).thenReturn(Optional.of("token-expires-at"));


            MvcResult registerUserMvcResult = mockMvc.perform(
                    post("/auth/register")
                            .contentType("application/json")
                            .accept("application/json")
                            .content(objectMapper.writeValueAsString(registerRequest)))
                    .andExpect(status().isCreated())
                    .andDo(print())
                    .andReturn();


            AuthResponse registerUserResponse = objectMapper.readValue(
                    registerUserMvcResult.getResponse().getContentAsString(),
                    AuthResponse.class
            );

            mockMvc.perform(
                            post("/auth/login")
                                    .contentType("application/json")
                                    .accept("application/json")
                                    .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(registerUserResponse)))
                    .andDo(print());
        }

        @Test
        public void shouldNotRegisterUserIsUserIsAlreadyExistsAndReturnErrorResponseWithStatus409Conflict() throws Exception {
            RegisterRequest registerRequest = RegisterRequestFactory.createRegisterRequest(faker);
            try(MockedStatic<LocalDateTime> localDateTimeMock = mockStatic(LocalDateTime.class, CALLS_REAL_METHODS)) {
                localDateTimeMock.when(LocalDateTime::now).thenReturn(localDateTimeNow);

                ReflectionTestUtils.setField(jwtService, "jwtSecretKey", "secret");
                when(jwtService.generateJWT(any())).thenCallRealMethod();
                when(jwtService.generateRefreshToken(any())).thenCallRealMethod();
                when(jwtService.getExpiresAt(any())).thenCallRealMethod();




                ErrorResponse errorResponse = ErrorResponse.builder()
                        .message("User is already exists")
                        .path("/auth/register")
                        .statusCode(409)
                        .date(LocalDateTime.now())
                        .build();
                mockMvc.perform(
                                post("/auth/register")
                                        .contentType("application/json")
                                        .accept("application/json")
                                        .content(objectMapper.writeValueAsString(registerRequest)))
                        .andExpect(status().isCreated())
                        .andDo(print());

                mockMvc.perform(
                                post("/auth/register")
                                        .contentType("application/json")
                                        .accept("application/json")
                                        .content(objectMapper.writeValueAsString(registerRequest)))
                        .andExpect(status().isConflict())
                        .andExpect(content().json(objectMapper.writeValueAsString(errorResponse)))
                        .andDo(print());
            }

        }

        @Test
        public void shouldNotRegisterUserIfRegisterRequestHaveNullFieldsAndReturnErrorResponseWithStatus422UnprocessableEntity() throws Exception {
            RegisterRequest registerRequest = RegisterRequest.builder().build();

            try(MockedStatic<LocalDateTime> localDateTimeMock = mockStatic(LocalDateTime.class)) {
                localDateTimeMock.when(LocalDateTime::now).thenReturn(localDateTimeNow);

                var usernameIsRequiredErrorField = ValidationErrorField.builder()
                        .field("username")
                        .message("username field is required")
                        .build();

                var usernameFieldNotShouldBeEmptyErrorField = ValidationErrorField.builder()
                        .field("username")
                        .message("username field not should be empty")
                        .build();

                var genreFieldShouldBeMaleOrFemaleErrorField = ValidationErrorField.builder()
                        .field("genre")
                        .message("genre field should be MALE or FEMALE")
                        .build();

                var emailIsRequiredErrorField = ValidationErrorField.builder()
                        .field("email")
                        .message("email field is required")
                        .build();

                var  passwordFieldIsRequired = ValidationErrorField.builder()
                        .field("password")
                        .message("password field is required")
                        .build();

                var  passwordFieldNotShouldBeEmptyErrorField = ValidationErrorField.builder()
                        .field("password")
                        .message("password field not should be empty")
                        .build();


                var genreFieldIsRequiredErrorField = ValidationErrorField.builder()
                        .field("genre")
                        .message("genre field is required")
                        .build();

                var birthDateFieldIsRequiredErrorField = ValidationErrorField.builder()
                        .field("birthDate")
                        .message("birthDate field is required")
                        .build();

                var birthDateFieldNotShouldBeEmptyErrorField = ValidationErrorField.builder()
                        .field("birthDate")
                        .message("birthDate field not should be empty")
                        .build();

                List<ValidationErrorField> validationErrorFields = List.of(
                        usernameIsRequiredErrorField,
                        usernameFieldNotShouldBeEmptyErrorField,
                        emailIsRequiredErrorField,
                        passwordFieldIsRequired,
                        passwordFieldNotShouldBeEmptyErrorField,
                        genreFieldIsRequiredErrorField,
                        genreFieldShouldBeMaleOrFemaleErrorField,
                        birthDateFieldIsRequiredErrorField,
                        birthDateFieldNotShouldBeEmptyErrorField
                );

                var validationErrorResponse = ValidationErrorResponse.validationErrorResponseBuilder()
                        .message("Validation error")
                        .path("/auth/register")
                        .statusCode(422)
                        .date(LocalDateTime.now())
                        .fields(validationErrorFields)
                        .build();

                mockMvc.perform(
                        post("/auth/register")
                                .contentType("application/json")
                                .accept("application/json")
                                .content(objectMapper.writeValueAsString(registerRequest))
                )
                        .andExpect(status().isUnprocessableEntity())
                        .andExpect(content().json(objectMapper.writeValueAsString(validationErrorResponse)))
                        .andDo(print());
            }

        }
    }
}