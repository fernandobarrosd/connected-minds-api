package com.fernando.connected_minds_api.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record UpdateCommentRequest(
        @NotBlank(message = "content field not should be empty")
        String content,

        @Positive(message = "likes field should be positive or zero")
        Long likes) {
}
