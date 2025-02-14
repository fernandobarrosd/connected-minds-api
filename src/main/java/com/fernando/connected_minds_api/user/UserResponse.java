package com.fernando.connected_minds_api.user;

import java.util.UUID;

import lombok.Builder;

@Builder
public record UserResponse(
    UUID userID,
    String username,
    String photoURL,
    String bannerURL,
    String bio) {

        public static UserResponse toResponse(User user) {
            return UserResponse.builder()
                .userID(user.getId())
                .username(user.getUsername())
                .photoURL(user.getPhotoURL())
                .bannerURL(user.getBannerURL())
                .bio(user.getBio())
                .build();
        }
    }
