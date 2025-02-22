package com.fernando.connected_minds_api.user;

import java.util.UUID;

import com.fernando.connected_minds_api.auth.LoginRequest;
import com.github.javafaker.Faker;
import java.time.LocalDate;
import com.fernando.connected_minds_api.formatters.DateTimeFormatters;

public final class UserFactory {
    private UserFactory() throws Exception {
        throw new Exception("UserFactory must not be instanced");
    }

    public static User createUser(Faker faker) {
        LocalDate thirtyYearsOldBirthDate = LocalDate.parse("09-10-2012", DateTimeFormatters.LOCAL_DATE_FORMATTER);

        UUID userID = UUID.randomUUID();
        String username = faker.name().username().replace(".", "_");
        String email = faker.internet().safeEmailAddress();
        String password = faker.internet().password(6, 7, true, false);
        String photoURL = faker.internet().image();
        String bio = faker.lorem().characters();
        UserGenre genre = UserGenre.MALE;

        return User.builder()
            .id(userID)
            .username(username)
            .email(email)
            .password(password)
            .photoURL(photoURL)
            .bio(bio)
            .genre(genre)
            .birthDate(thirtyYearsOldBirthDate)
            .build();

    }

    public static User createUser(Faker faker, LoginRequest loginRequest) {
        LocalDate thirtyYearsOldBirthDate = LocalDate.parse("09-10-2012", DateTimeFormatters.LOCAL_DATE_FORMATTER);

        UUID userID = UUID.randomUUID();
        String username = faker.name().username().replace(".", "_");
        String email = loginRequest.email();
        String password = loginRequest.password();
        String photoURL = faker.internet().image();
        String bio = faker.lorem().characters();
        UserGenre genre = UserGenre.MALE;

        return User.builder()
                .id(userID)
                .username(username)
                .email(email)
                .password(password)
                .photoURL(photoURL)
                .bio(bio)
                .genre(genre)
                .birthDate(thirtyYearsOldBirthDate)
                .build();
    }
}