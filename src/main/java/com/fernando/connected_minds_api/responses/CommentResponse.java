package com.fernando.connected_minds_api.responses;

import com.fernando.connected_minds_api.models.Comment;
import com.fernando.connected_minds_api.models.User;
import lombok.Builder;
import java.util.UUID;

@Builder
public record CommentResponse(
        UUID id,
        String content,
        Long likes,
        String createdAt,
        UserResponse owner,
        UUID postID) {
    public static CommentResponse toResponse(Comment comment) {
        User owner = comment.getOwner();

        var ownerResponse = UserResponse.toResponse(owner);

        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .likes(comment.getLikes().stream().count())
                .createdAt(comment.getCreatedAt().toString())
                .postID(comment.getPost().getId())
                .owner(ownerResponse)
                .build();
    }
}