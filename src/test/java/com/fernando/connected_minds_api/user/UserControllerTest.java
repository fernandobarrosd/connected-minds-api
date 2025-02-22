package com.fernando.connected_minds_api.user;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.mockito.Mockito.mockStatic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.connected_minds_api.error.ErrorResponse;
import com.fernando.connected_minds_api.formatters.DateTimeFormatters;
import com.fernando.connected_minds_api.singleton.FakerSingleton;
import com.github.javafaker.Faker;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    private User userSaved;

    private Faker faker;

    private LocalDateTime localDateTimeNow;

    @BeforeEach
    public void setup() {
        faker = FakerSingleton.getInstance();
        localDateTimeNow = LocalDateTime.parse("20-12-2025 00:00:00", DateTimeFormatters.LOCAL_DATE_TIME_FORMATTER);
    }

    @Nested
    public class FindUserByUsernameTests {
        @Test
        public void shouldFindUserByUsernameAndReturnResponseWithStatus200Ok() throws Exception {
            user = UserFactory.createUser(faker);
            userSaved = userRepository.save(user);
            String input = user.getUsername();

            MvcResult findUserByUsernameMvcResult1 = mockMvc.perform(
                get("/users/%s".formatted(input))
                .accept("application/json"))
            .andExpect(status().isOk())
            .andReturn();


            UserResponse expectedUserResponse = objectMapper.readValue(
                findUserByUsernameMvcResult1.getResponse().getContentAsString(),
                UserResponse.class
            );

            mockMvc.perform(
                get("/users/%s".formatted(input))
                .accept("application/json"))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(expectedUserResponse)));
            
        }

        @Test
        public void shouldReturnResponseWithStatus404WhenUserIsNotFound() throws Exception {
            String input = "user_test";
            String path = "/users/%s".formatted(input);
            
            try(MockedStatic<LocalDateTime> localDateTimeMock = mockStatic(LocalDateTime.class)) {
                localDateTimeMock.when(LocalDateTime::now).thenReturn(localDateTimeNow);

                ErrorResponse errorResponse = ErrorResponse.builder()
                .message("User is not found")
                .path(path)
                .date(LocalDateTime.now())
                .statusCode(404)
                .build();

                mockMvc.perform(
                    get(path)
                    .accept("application/json")
                )
                .andExpect(status().isNotFound())
                .andExpect(content().json(objectMapper.writeValueAsString(errorResponse)))
                .andDo(print());
            }
        }
    }
}