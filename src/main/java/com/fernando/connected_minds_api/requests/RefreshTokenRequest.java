package com.fernando.connected_minds_api.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record RefreshTokenRequest(
        @NotNull(message = "refreshToken field is required")
        @NotEmpty(message = "refreshToken field not should be empty")
        String refreshToken) {}
