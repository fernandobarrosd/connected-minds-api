package com.fernando.connected_minds_api.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.connected_minds_api.errors.ErrorResponse;
import com.fernando.connected_minds_api.errors.validation.ValidationErrorField;
import com.fernando.connected_minds_api.errors.validation.ValidationErrorResponse;
import com.fernando.connected_minds_api.formatters.DateTimeFormatters;
import com.fernando.connected_minds_api.singleton.FakerSingleton;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.mockStatic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import java.time.LocalDateTime;
import java.util.List;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerTest {
    private User user;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private final LocalDateTime localDateTimeNow = LocalDateTime.parse("20-12-2025 00:00:00",
            DateTimeFormatters.LOCAL_DATE_TIME_FORMATTER);

    private final Faker FAKER = FakerSingleton.getInstance();


    @Test
    public void shouldFindUserByUsernameWithSuccessAndReturnUserResponseWithStatus200Ok() throws Exception {
        User user = userRepository.save(UserFactory.createUserWithoutID(FAKER));

        String username = user.getUsername();
        UserResponse userResponse = user.toResponse();

        mockMvc.perform(
                get("/users/" + username)
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userResponse)));
    }

    @Test
    public void shouldNotFindUserByUsernameIfUserUsernameWasNotFoundAndReturnErrorResponseStatusNotFound404() throws Exception {
        try(MockedStatic<LocalDateTime> localDateTimeMock = mockStatic(LocalDateTime.class)) {
            localDateTimeMock.when(LocalDateTime::now).thenReturn(localDateTimeNow);

            String username = "username_test";
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .message("User not found")
                    .path("/users/" + username)
                    .statusCode(HttpStatus.NOT_FOUND)
                    .date(LocalDateTime.now())
                    .build();

            mockMvc.perform(
                            get("/users/" + username)
                                    .accept("application/json"))
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(objectMapper.writeValueAsString(errorResponse)));
        }
    }

    @Test
    public void shouldReturnStatus422UnprocessableEntityIfUsernameSizeWasLessThan4Characters() throws Exception {
        try(MockedStatic<LocalDateTime> localDateTimeMock = mockStatic(LocalDateTime.class)) {
            localDateTimeMock.when(LocalDateTime::now).thenReturn(localDateTimeNow);

            String invalidUsername = "A";

            var invalidUsernameErrorField = ValidationErrorField.builder()
                    .field("username")
                    .message("username parameter should be at min 4 character")
                    .build();

            var validationErrorResponse = ValidationErrorResponse.builder()
                    .path("/users/" + invalidUsername)
                    .message("Validation error")
                    .date(LocalDateTime.now())
                    .fields(List.of(invalidUsernameErrorField))
                    .build();

            mockMvc.perform(
                            get("/users/" + invalidUsername)
                                    .accept("application/json"))
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(content().json(objectMapper.writeValueAsString(validationErrorResponse)))
                    .andDo(print());
        }
    }

    @Test
    public void shouldReturnStatus422UnprocessableEntityIfUsernameFormatIsInvalid() throws Exception {
        try(MockedStatic<LocalDateTime> localDateTimeMock = mockStatic(LocalDateTime.class)) {
            localDateTimeMock.when(LocalDateTime::now).thenReturn(localDateTimeNow);

            String invalidUsername = "invalid-username";

            var invalidUsernameErrorField = ValidationErrorField.builder()
                    .field("username")
                    .message("username parameter must have lower case and uppercase letters, numbers and include this characters: [_]")
                    .build();

            var validationErrorResponse = ValidationErrorResponse.builder()
                    .path("/users/" + invalidUsername)
                    .message("Validation error")
                    .date(LocalDateTime.now())
                    .fields(List.of(invalidUsernameErrorField))
                    .build();

            mockMvc.perform(
                            get("/users/" + invalidUsername)
                                    .accept("application/json"))
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(content().json(objectMapper.writeValueAsString(validationErrorResponse)))
                    .andDo(print());
        }
    }
}