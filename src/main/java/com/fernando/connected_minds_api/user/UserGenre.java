package com.fernando.connected_minds_api.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserGenre {
    MALE("Male"),
    FEMALE("Female");

    private final String value;
}
