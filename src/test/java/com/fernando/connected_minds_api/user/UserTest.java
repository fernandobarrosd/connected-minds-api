package com.fernando.connected_minds_api.user;

import com.fernando.connected_minds_api.singleton.FakerSingleton;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserTest {
    private final Faker FAKER = FakerSingleton.getInstance();

    @Test
    @DisplayName("Should create user with success")
    public void shouldCreateUserWithSuccess() {
        User expectedUser = UserFactory.createUser(FAKER);
        User user = new User(expectedUser);

        assertEquals(expectedUser.getId(), user.getId());
        assertEquals(expectedUser.getUsername(), user.getUsername());
        assertEquals(expectedUser.getEmail(), user.getEmail());
        assertEquals(expectedUser.getPassword(), user.getPassword());
        assertEquals(expectedUser.getPhotoURL(), user.getPhotoURL());
        assertEquals(expectedUser.getBannerURL(), user.getBannerURL());
        assertEquals(expectedUser.getBio(), user.getBio());
        assertEquals(expectedUser.getBirthDate(), user.getBirthDate());

    }

    @Test
    @DisplayName("Should not create user with invalid age and throws UnderAgeException")
    public void shouldNotCreateUserWithInvalidAgeAndThrowsUnderAgeException() {
        var exception = assertThrows(
                UserUnderAgeException.class,
                () -> UserFactory.createUserWithInvalidAge(FAKER)
        );

        assertEquals("User age must be greater than or equal that 13 years old", exception.getMessage());
    }

    @Test
    @DisplayName("Should convert User to UserResponse")
    public void shouldConvertUserToUserResponse() {
        User user = UserFactory.createUser(FAKER);
        UserResponse response = user.toResponse();

        assertEquals(user.getId(), response.userID());
        assertEquals(user.getUsername(), response.username());
        assertEquals(user.getPhotoURL(), response.photoURL());
        assertEquals(user.getBannerURL(), response.bannerURL());
        assertEquals(user.getBio(), response.bio());
        assertEquals(user.getGenre(), response.genre());
    }
}