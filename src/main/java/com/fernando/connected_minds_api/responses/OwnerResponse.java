package com.fernando.connected_minds_api.responses;

import com.fernando.connected_minds_api.models.User;
import lombok.Builder;
import java.util.UUID;

@Builder
public record OwnerResponse(UUID id, String photoURL, String username) {

    public static OwnerResponse toResponse(User owner) {
        return OwnerResponse.builder()
                .id(owner.getId())
                .username(owner.getUsername())
                .photoURL(owner.getPhotoURL())
                .build();
    }
}