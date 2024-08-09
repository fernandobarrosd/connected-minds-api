package com.fernando.connected_minds_api.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TagRequest(
    String id,

    @NotNull(message = "name is required")
    @NotBlank(message = "name not should be empty")
    String name) {}
