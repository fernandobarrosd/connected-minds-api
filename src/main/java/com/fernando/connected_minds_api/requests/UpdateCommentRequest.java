package com.fernando.connected_minds_api.requests;

import jakarta.validation.constraints.NotBlank;

public record UpdateCommentRequest(
        @NotBlank(message = "content field not should be empty")
        String content) {}