package com.fernando.connected_minds_api.responses;

import java.util.UUID;
import com.fernando.connected_minds_api.models.Post;
import com.fernando.connected_minds_api.models.User;
import lombok.Builder;
import java.util.List;

@Builder
public record CreatePostResponse(
        UUID id,
        String content,
        String createdAt,
        MediaResponse media,
        Long likes,
        UUID locationID,
        List<String> locationMembersUsernames,
        UUID notificationID,
        UserResponse owner) {
            
            
        public static CreatePostResponse toResponse(Post post, List<String> locationMembersUsernames, UUID notificationID) {
            User owner = post.getOwner();

            var ownerResponse = UserResponse.toResponse(owner);
            var mediaResponse = MediaResponse.toResponse(post.getMedia());

            return CreatePostResponse.builder()
                    .id(post.getId())
                    .locationID(post.getLocationID())
                    .content(post.getContent())
                    .likes(post.getLikes().stream().count())
                    .media(mediaResponse)
                    .createdAt(post.getCreatedAt().toString())
                    .owner(ownerResponse)
                    .locationMembersUsernames(locationMembersUsernames)
                    .notificationID(notificationID)
                    .build();
    }
}