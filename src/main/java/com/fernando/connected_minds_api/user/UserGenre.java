package com.fernando.connected_minds_api.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum UserGenre {
    MALE("Male"),
    FEMALE("Female");

    private final String value;
}