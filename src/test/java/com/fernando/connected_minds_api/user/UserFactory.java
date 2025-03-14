package com.fernando.connected_minds_api.user;

import com.fernando.connected_minds_api.formatters.DateTimeFormatters;
import com.github.javafaker.Faker;
import java.time.LocalDate;
import java.util.UUID;

public final class UserFactory {
    private UserFactory() throws Exception {
        throw new Exception("UserFactory must not be instanced");
    }

    public static User createUser(Faker faker) {
        LocalDate thirtyYearsOldBirthDate = LocalDate.parse("09-10-2012", DateTimeFormatters.LOCAL_DATE_FORMATTER);

        UUID userID = UUID.randomUUID();
        String username = faker.name().username().replace(".", "_");
        String email = username + "@test.com";
        String password = faker.internet().password(6, 7, true, false);
        String photoURL = faker.internet().image();
        String bannerURL = faker.internet().image();
        String bio = faker.lorem().characters();
        UserGenre genre = UserGenre.MALE;

        return new User(userID, username, email, password, photoURL,
                bannerURL, bio, genre, thirtyYearsOldBirthDate);

    }

    public static User createUserWithoutID(Faker faker) {
        LocalDate thirtyYearsOldBirthDate = LocalDate.parse("09-10-2012", DateTimeFormatters.LOCAL_DATE_FORMATTER);

        String username = faker.name().username().replace(".", "_");
        String email = username + "@test.com";
        String password = faker.internet().password(6, 7, true, false);
        String photoURL = faker.internet().image();
        String bannerURL = faker.internet().image();
        String bio = faker.lorem().characters();
        UserGenre genre = UserGenre.MALE;

        return new User(username, email, password, photoURL,
                bannerURL, bio, genre, thirtyYearsOldBirthDate);

    }

    public static User createUserWithInvalidAge(Faker faker) {
        LocalDate thirtyYearsOldBirthDate = LocalDate.parse("09-10-2013", DateTimeFormatters.LOCAL_DATE_FORMATTER);

        UUID userID = UUID.randomUUID();
        String username = faker.name().username().replace(".", "_");
        String email = username + "@test.com";
        String password = faker.internet().password(6, 7, true, false);
        String photoURL = faker.internet().image();
        String bannerURL = faker.internet().image();
        String bio = faker.lorem().characters();
        UserGenre genre = UserGenre.MALE;

        return new User(userID, username, email, password, photoURL,
                bannerURL, bio, genre, thirtyYearsOldBirthDate);
    }
}
