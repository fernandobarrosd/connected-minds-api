package com.fernando.connected_minds_api.auth.factories;

import com.fernando.connected_minds_api.auth.RegisterRequest;
import com.fernando.connected_minds_api.formatters.DateTimeFormatters;
import com.fernando.connected_minds_api.user.UserGenre;
import com.github.javafaker.Faker;
import java.time.LocalDate;

public final class RegisterRequestFactory {
    private RegisterRequestFactory() throws Exception {
        throw new Exception("RegisterRequestFactory must not be instanced");
    }

    public static RegisterRequest createRegisterRequest(Faker faker) {
        LocalDate thirtyYearsOldBirthDate = LocalDate.parse("09-10-2012", DateTimeFormatters.LOCAL_DATE_FORMATTER);

        String username = faker.name().username().replace(".", "_");
        String email = faker.internet().safeEmailAddress();
        String password = faker.internet().password(6, 7, true, false);
        String bio = faker.lorem().characters(8);
        UserGenre genre = UserGenre.MALE;

        return RegisterRequest.builder()
                .email(email)
                .password(password)
                .username(username)
                .bio(bio)
                .genre(genre.name())
                .birthDate(thirtyYearsOldBirthDate.toString())
                .build();
    }

    public static RegisterRequest createRegisterRequestWithAgeLessThan13(Faker faker) {
        LocalDate thirtyYearsOldBirthDate = LocalDate.parse("09-10-2013", DateTimeFormatters.LOCAL_DATE_FORMATTER);

        String username = faker.name().username().replace(".", "_");
        String email = faker.internet().safeEmailAddress();
        String password = faker.internet().password(6, 7, true, false);
        String bio = faker.lorem().characters();
        UserGenre genre = UserGenre.MALE;

        return RegisterRequest.builder()
                .email(email)
                .password(password)
                .username(username)
                .bio(bio)
                .genre(genre.name())
                .birthDate(thirtyYearsOldBirthDate.toString())
                .build();
    }
}
