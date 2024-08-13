package com.fernando.connected_minds_api.requests;

import java.util.List;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record GroupRequest(
        @NotNull(message = "name field is required")
        @NotEmpty(message = "name field not should be empty")
        String name,

        @NotNull(message = "description field is required")
        @NotEmpty(message = "description field not should be empty")

        String description,

        @NotNull(message = "tags field is required")
        List<TagRequest> tags,

        @NotNull(message = "photoURL field is required")
        String photoURL,

        @NotNull(message = "bannerURL field is required")
        String bannerURL) {}