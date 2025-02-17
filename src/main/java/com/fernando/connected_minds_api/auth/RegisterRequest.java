package com.fernando.connected_minds_api.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import java.time.LocalDate;
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
            message = "username field must have lower case and upercase letters, numbers and include this characters: [_]")
        String username,

        @NotNull(message = "email field is required")
        @Email(message = "E-mail field is not valid")
        String email,

        @NotNull(message = "password field is required")
        @NotEmpty(message = "password field not should be empty")
        @Length(min = 6, message = "password field must be 6 characteres")
        String password,

        @URL(message = "photoURL field should be url")
        String photoURL,

        @URL(message = "bannerURL field should be url")
        String bannerURL,

        String bio,

        @NotNull(message = "genre field is required")
        @Enum(value = {"MALE", "FEMALE"}, message = "genre field should be MALE or FEMALE")
        String genre,

        @NotNull(message = "birthDate fied is required")
        @NotEmpty(message = "birthDate field not should be empty")
        String birthDate) {

    public User toEntity() {
        LocalDate birthDateFormatted = LocalDate.parse(birthDate);

        return User.builder()
            .username(username)
            .email(email)
            .password(password)
            .photoURL(photoURL)
            .bannerURL(bannerURL)
            .bio(bio)
            .genre(UserGenre.valueOf(genre))
            .birthDate(birthDateFormatted)
            .build();
    }
}