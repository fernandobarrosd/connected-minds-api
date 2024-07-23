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
        OwnerResponse owner,
        UUID postID) {
    public static CommentResponse toResponse(Comment comment) {
        User owner = comment.getOwner();

        var ownerResponse = OwnerResponse.builder()
                .id(owner.getId())
                .username(owner.getUsername())
                .photoURL(owner.getPhotoURL())
                .build();

        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .likes(comment.getLikes())
                .createdAt(comment.getCreatedAt().toString())
                .owner(ownerResponse)
                .postID(comment.getPost().getId())
                .build();
    }
}