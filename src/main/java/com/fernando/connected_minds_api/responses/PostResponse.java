package com.fernando.connected_minds_api.responses;

import lombok.Builder;

@Builder
public record PostResponse(
        String id,
        String content,
        String createdAt,
        String photoURL,
        Long likes,
        Owner owner) {

    @Builder
    public record Owner(
            String id,
            String username,
            String photoURL) {}
}