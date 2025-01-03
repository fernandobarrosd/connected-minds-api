package com.fernando.connected_minds_api.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum MediaType {
    IMAGE("Image"),
    VIDEO("Video"),
    AUDIO("Audio");

    private final String value;
}