package com.fernando.connected_minds_api.auth;

import com.fernando.connected_minds_api.formatters.DateTimeFormatters;
import com.fernando.connected_minds_api.validation.constraints.LocalDateFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import java.time.LocalDate;
import java.util.UUID;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;
import com.fernando.connected_minds_api.user.User;
import com.fernando.connected_minds_api.user.UserGenre;
import com.fernando.connected_minds_api.validation.constraints.Enum;

@Builder
public record RegisterRequest(
        @NotNull(message = "username field is required")
        @NotEmpty(message = "username field not should be empty")
        @Pattern(
            regexp = "[A-Za-z0-9_]+",
            message = "username field must have lower case and uppercase letters, numbers and include this characters: [_]")
        String username,

        @NotNull(message = "email field is required")
        @Email(message = "E-mail field is not valid")
        String email,

        @NotNull(message = "password field is required")
        @NotEmpty(message = "password field not should be empty")
        @Length(min = 6, message = "password field must be 6 characters")
        String password,

        String bio,

        @NotNull(message = "genre field is required")
        @Enum(value = {"MALE", "FEMALE"}, message = "genre field should be MALE or FEMALE")
        String genre,

        @NotNull(message = "birthDate field is required")
        @NotEmpty(message = "birthDate field not should be empty")
        @LocalDateFormat(message = "birthDate should be pattern dd-mm-yyy (day, month, and year)")
        String birthDate) {

    public User toEntity() {
        LocalDate birthDateLocalDate = LocalDate.parse(birthDate, DateTimeFormatters.LOCAL_DATE_FORMATTER);

        return User.builder()
                .id(UUID.randomUUID())
                .username(username)
                .email(email)
                .password(password)
                .bio(bio)
                .genre(UserGenre.valueOf(genre))
                .birthDate(birthDateLocalDate)
                .build();
    }
}