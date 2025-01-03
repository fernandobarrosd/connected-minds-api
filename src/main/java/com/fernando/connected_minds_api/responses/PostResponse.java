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
        MediaResponse media,
        Long likes,
        UUID locationID,
        UserResponse owner,
        Long commentsCount) {

    public static PostResponse toResponse(Post post) {
        User owner = post.getOwner();

        var ownerResponse = UserResponse.toResponse(owner);
        var mediaResponse = MediaResponse.toResponse(post.getMedia());
        
        return PostResponse.builder()
                .id(post.getId())
                .locationID(post.getLocationID())
                .content(post.getContent())
                .likes(post.getLikes().stream().count())
                .media(mediaResponse)
                .createdAt(post.getCreatedAt().toString())
                .owner(ownerResponse)
                .commentsCount(post.getComments().stream().count())
                .build();
    }
}