package com.fernando.connected_minds_api.requests;

import com.fernando.connected_minds_api.annotations.EnumValidator;
import com.fernando.connected_minds_api.enums.UserGenre;
import com.fernando.connected_minds_api.models.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import org.hibernate.validator.constraints.Length;

public record RegisterRequest(
        @NotNull(message = "username field is required")
        @NotEmpty(message = "username field not should be empty")
        @Pattern(
            regexp = "[A-Za-z0-9_]+",
            message = "username field must have lower case and upercase letters, numbers and include this characters: [_]")
        String username,

        @NotNull(message = "email field is required")
        @Email(message = "E-mail field is not valid")
        String email,

        @NotNull(message = "password field is required")
        @NotEmpty(message = "password field not should be empty")
        @Length(min = 6, message = "password field must be a minimun 6 characteres")
        String password,

        String photoURL,

        String bannerURL,

        @NotEmpty(message = "bio field not should be empty")
        String bio,

        @NotNull(message = "genre field is required")
        @EnumValidator(enumValues = {"MALE", "FEMALE"}, message = "genre field should be MALE or FEMALE")
        String genre,

        @NotNull(message = "birthDate fied is required")
        @NotEmpty(message = "birthDate field not should be empty")
        String birthDate) {

    public User toEntity() {
        LocalDate birthDateFormatted = LocalDate.parse(birthDate);
        return new User(username, email, password,
                birthDateFormatted, photoURL, bannerURL, bio, 
                UserGenre.valueOf(genre));
    }
}