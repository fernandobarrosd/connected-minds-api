package com.fernando.connected_minds_api.responses;

import com.fernando.connected_minds_api.models.Comment;
import lombok.Builder;

import java.util.UUID;

@Builder
public record CommentResponse(
        UUID id,
        String content,
        Long likes,
        String createdAt,
        UUID ownerID,
        UUID postID) {
    public static CommentResponse fromEntity(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .likes(comment.getLikes())
                .createdAt(comment.getCreatedAt().toString())
                .ownerID(comment.getOwner().getId())
                .postID(comment.getPost().getId())
                .build();
    }
}