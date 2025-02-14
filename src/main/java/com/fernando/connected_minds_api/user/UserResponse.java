package com.fernando.connected_minds_api.user;

import java.util.UUID;

import lombok.Builder;

@Builder
public record UserResponse(
    UUID userId,
    String username,
    String photoURL,
    String bannerURL,
    String bio) {

        public static UserResponse toResponse(User user) {
            return UserResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .photoURL(user.getPhotoURL())
                .bannerURL(user.getBannerURL())
                .bio(user.getBio())
                .build();
        }
    }
