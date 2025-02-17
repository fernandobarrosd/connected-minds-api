package com.fernando.connected_minds_api.auth;

import org.hibernate.validator.constraints.Length;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotNull(message = "email field is required")
        @Email(message = "E-mail is not valid")
        String email,

        @NotNull(message = "password field is required")
        @NotBlank(message = "password field not should be empty")
        @Length(min = 6, message = "password field should be min 6 characters")
        String password) {}