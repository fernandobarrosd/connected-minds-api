package com.fernando.connected_minds_api.requests;

import com.fernando.connected_minds_api.annotations.EnumValidator;
import com.fernando.connected_minds_api.enums.UserGenre;
import com.fernando.connected_minds_api.models.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record RegisterRequest(
        @NotNull(message = "username is required")
        @NotEmpty(message = "username not should be empty")
        String username,

        @NotNull(message = "E-mail is required")
        @Email(message = "E-mail is not valid")
        String email,

        @NotNull(message = "password is required")
        @NotEmpty(message = "password not should be empty")
        String password,

        @NotNull(message = "photoURL is required")
        @NotEmpty(message = "photoURL not should be empty")
        String photoURL,

        @NotNull(message = "bannerURL is required")
        @NotEmpty(message = "bannerURL not should be empty")
        String bannerURL,

        @NotNull(message = "genre is required")
        @EnumValidator(enumValues = {"MALE", "FEMALE"}, message = "User genre should be MALE or FEMALE")
        String genre,

        @NotNull(message = "birthDate is required")
        @NotEmpty(message = "birthDate not should be empty")
        String birthDate) {

    public User toEntity() {
        LocalDate birthDateFormatted = LocalDate.parse(birthDate);
        return new User(username, email, password,
                birthDateFormatted, photoURL, bannerURL, UserGenre.valueOf(genre));
    }
}