package com.fernando.connected_minds_api.requests;

import java.util.UUID;
import com.fernando.connected_minds_api.annotations.EnumValidator;
import com.fernando.connected_minds_api.enums.PostLocationType;
import jakarta.validation.constraints.NotNull;

public record FindAllPostsRequest(
    @NotNull(message = "locationID field is required")
    UUID locationID,
        
    @NotNull(message = "content field is required")
    @EnumValidator(enumValues = {"COMMUNITY", "GROUP"},  message = "locationType field should be COMMUNITY or GROUP")
    PostLocationType locationType) {}