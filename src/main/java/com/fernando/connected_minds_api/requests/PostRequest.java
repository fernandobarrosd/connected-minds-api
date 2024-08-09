package com.fernando.connected_minds_api.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PostRequest(
        @NotNull(message = "content is required")
        @NotBlank(message = "content not should be empty")
        String content,

        String photoURL,
        
        @NotNull(message = "locationID field is required")
        String locationID) {}