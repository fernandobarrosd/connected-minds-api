package com.fernando.connected_minds_api.responses;

import lombok.Builder;

import java.util.UUID;

@Builder
public record CommentResponse(
        UUID id,
        String content,
        Long likes,
        String createdAt,
        UUID ownerID,
        UUID postID) {}