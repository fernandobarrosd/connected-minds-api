package com.fernando.connected_minds_api.responses;

import lombok.Builder;
import java.util.UUID;

@Builder
public record OwnerResponse(
        UUID id,
        String photoURL,
        String username) {}