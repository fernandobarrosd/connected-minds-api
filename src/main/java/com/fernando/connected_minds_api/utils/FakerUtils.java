package com.fernando.connected_minds_api.utils;

import java.time.LocalDate;
import java.util.UUID;
import com.fernando.connected_minds_api.enums.PostLocationType;
import com.fernando.connected_minds_api.enums.UserGenre;
import com.fernando.connected_minds_api.models.Post;
import com.fernando.connected_minds_api.models.User;
import com.fernando.connected_minds_api.requests.PostRequest;
import net.datafaker.Faker;


public final class FakerUtils {
    private FakerUtils() {
        String className = getClass().getSimpleName();
        throw new UnsupportedOperationException(
            "%s class must not be instanced".formatted(className)
        );
    }

    public static User generateFakeUser(Faker faker) {

        String ownerUsername = faker.regexify("username_[A-Za-z]{4,8}");
        String ownerEmail = faker.internet().emailAddress();
        String ownerPassword = faker.internet().password(8, 8);
        UserGenre genre = faker.options().option(UserGenre.class);

        return new User(
            UUID.randomUUID(),
            ownerUsername,
            ownerEmail,
            ownerPassword,
            LocalDate.now(),
            null,
            null,
            genre
        );
    }

    public static PostRequest generateFakePostRequest(Faker faker, PostLocationType locationType) {
        String postContent = faker.lorem().characters(6);

        return new PostRequest(
            postContent,
            null,
            UUID.randomUUID(),
            locationType
        );
    }

    public static Post generateFakePost(Faker faker, User owner) {
        String postContent = faker.lorem().characters(6);
        return new Post(
            postContent,
            null,
            UUID.randomUUID(),
            owner
        );
    }
}