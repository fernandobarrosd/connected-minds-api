package com.fernando.connected_minds_api.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record UpdateCommentRequest(
        @NotBlank(message = "content not should be empty")
        String content,

        @Positive(message = "likes should be positive")
        Long likes) {
}
