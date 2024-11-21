package com.fernando.connected_minds_api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NotificationType {
    POST("Post"),
    COMMENT("Comment"),
    LIKE_POST("Like Post"),
    LIKE_COMMENT("Like Comment");

    private String value;
}