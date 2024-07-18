package com.fernando.connected_minds_api.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentRequest(
        @NotNull(message = "content is required")
        @NotBlank(message = "content not should be empty")
        String content) {}