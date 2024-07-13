package com.fernando.connected_minds_api.requests;

import com.fernando.connected_minds_api.enums.UserGenre;
import com.fernando.connected_minds_api.models.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
        String photoURL,
        String bannerURL,

        @NotNull(message = "genre is required")
        @NotEmpty(message = "genre not should be empty")
        @Pattern(regexp = "Male|Female", message = "User genre should be Male or Female")
        UserGenre genre,

        @NotNull(message = "birthDate is required")
        @NotEmpty(message = "birthDate not should be empty")
        String birthDate) {

    public User toEntity() {
        LocalDate birthDateFormatted = LocalDate.parse(birthDate);
        return new User(username, email, password,
                birthDateFormatted, photoURL, bannerURL, genre);
    }
}