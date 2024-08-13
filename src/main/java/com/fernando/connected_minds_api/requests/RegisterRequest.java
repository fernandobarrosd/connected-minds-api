package com.fernando.connected_minds_api.requests;

import com.fernando.connected_minds_api.annotations.EnumValidator;
import com.fernando.connected_minds_api.enums.UserGenre;
import com.fernando.connected_minds_api.models.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;

public record RegisterRequest(
        @NotNull(message = "username field is required")
        @NotEmpty(message = "username field not should be empty")
        @Pattern(
            regexp = "^[a-z][a-zA-Z0-9_]*",
            message = "username field should be starts with lower case letter and can include this characters: [a-z], [A-Z], [0-9], _")
        String username,

        @NotNull(message = "email field is required")
        @Email(message = "E-mail field is not valid")
        String email,

        @NotNull(message = "password field is required")
        @NotEmpty(message = "password field not should be empty")
        String password,

        @NotNull(message = "photoURL field is required")
        String photoURL,

        @NotNull(message = "bannerURL field is required")
        String bannerURL,

        @NotNull(message = "genre field is required")
        @EnumValidator(enumValues = {"MALE", "FEMALE"}, message = "genre field should be MALE or FEMALE")
        String genre,

        @NotNull(message = "birthDate fied is required")
        @NotEmpty(message = "birthDate field not should be empty")
        String birthDate) {

    public User toEntity() {
        LocalDate birthDateFormatted = LocalDate.parse(birthDate);
        return new User(username, email, password,
                birthDateFormatted, photoURL, bannerURL, UserGenre.valueOf(genre));
    }
}