package com.fernando.connected_minds_api.requests;

import java.util.UUID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record TagRequest(
    UUID id,

    @NotNull(message = "name field is required")
    @NotBlank(message = "name field not should be empty")
    @Pattern(regexp = "^[a-z][a-zA-Z0-9_]*", message = "tag name should be starts with lower case letter and can include this characters: [a-z], [A-Z], [0-9], _")
    String name) {}
