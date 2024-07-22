package com.fernando.connected_minds_api.responses;

import com.fernando.connected_minds_api.models.Post;
import com.fernando.connected_minds_api.models.User;
import lombok.Builder;
import java.util.UUID;

@Builder
public record PostResponse(
        UUID id,
        String content,
        String createdAt,
        String photoURL,
        Long likes,
        OwnerResponse owner) {

    public static PostResponse fromEntity(Post post) {
        User owner = post.getOwner();

        OwnerResponse ownerResponse = OwnerResponse.builder()
                .id(owner.getId())
                .username(owner.getUsername())
                .photoURL(owner.getPhotoURL())
                .build();

        return PostResponse.builder()
                .id(post.getId())
                .content(post.getContent())
                .likes(post.getLikes())
                .photoURL(post.getPhotoURL())
                .owner(ownerResponse)
                .createdAt(post.getCreatedAt().toString())
                .build();
    }
}