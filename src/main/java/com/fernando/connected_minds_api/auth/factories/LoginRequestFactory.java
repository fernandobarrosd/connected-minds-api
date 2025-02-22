package com.fernando.connected_minds_api.auth.factories;

import com.fernando.connected_minds_api.auth.LoginRequest;
import com.github.javafaker.Faker;

public final class LoginRequestFactory {
    private LoginRequestFactory() throws Exception {
        throw new Exception("LoginRequestFactory must not be instanced");
    }

    public static LoginRequest createLoginRequest(Faker faker) {
        String email = faker.internet().safeEmailAddress();
        String password = faker.internet().password(6, 7, true, false);

        return LoginRequest.builder()
                .email(email)
                .password(password)
                .build();
    }

    public static LoginRequest createLoginRequestWithInvalidEmail(Faker faker) {
        String email = "invalid-email";
        String password = faker.internet().password(6, 7, true, false);

        return LoginRequest.builder()
                .email(email)
                .password(password)
                .build();
    }

    public static LoginRequest createLoginRequestWithInvalidPassword(Faker faker) {
        String email = faker.internet().safeEmailAddress();
        String password = faker.internet().password(5, 6, true, false);

        return LoginRequest.builder()
                .email(email)
                .password(password)
                .build();
    }
}
