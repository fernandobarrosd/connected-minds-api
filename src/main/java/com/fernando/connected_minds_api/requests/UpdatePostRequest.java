package com.fernando.connected_minds_api.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record UpdatePostRequest(
        @NotBlank(message = "content field not should be empty")
        String content,

        @NotBlank(message = "photoURL field not should be empty")
        String photoURL,

        @PositiveOrZero(message = "likes field should be positive or zero")
        Long likes) {}