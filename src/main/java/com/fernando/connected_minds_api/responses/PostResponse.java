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
        UUID locationID,
        OwnerResponse owner) {

    public static PostResponse toResponse(Post post) {
        User owner = post.getOwner();

        OwnerResponse ownerResponse = OwnerResponse.toResponse(owner);

        return PostResponse.builder()
                .id(post.getId())
                .locationID(post.getLocationID())
                .content(post.getContent())
                .likes((long) post.getLikes().size())
                .photoURL(post.getPhotoURL())
                .owner(ownerResponse)
                .createdAt(post.getCreatedAt().toString())
                .build();
    }
}