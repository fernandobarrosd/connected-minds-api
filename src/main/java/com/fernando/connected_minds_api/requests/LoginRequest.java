package com.fernando.connected_minds_api.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotNull(message = "E-mail is required")
        @Email(message = "E-mail is not valid")
        String email,

        @NotNull(message = "Password is required")
        @NotBlank(message = "Password not should be empty")
        String password) {}