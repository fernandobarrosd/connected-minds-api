package com.fernando.connected_minds_api.requests;

import jakarta.validation.constraints.NotBlank;

public record UpdatePostRequest(
        @NotBlank(message = "content field not should be empty")
        String content,

        @NotBlank(message = "photoURL field not should be empty")
        String photoURL) {}