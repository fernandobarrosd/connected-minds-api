package com.fernando.connected_minds_api.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record RefreshTokenRequest(
        @NotNull(message = "refreshToken is required")
        @NotEmpty(message = "refreshToken not should be empty")
        String refreshToken) {}
