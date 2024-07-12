package com.fernando.connected_minds_api.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum UserStatus {
    ONLINE("Online"),
    OFFLINE("Offline");

    private final String value;
}