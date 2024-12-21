package com.fernando.connected_minds_api.responses;

import com.fernando.connected_minds_api.models.User;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String username,
        String genre,
        String bio,
        String photoURL,
        String bannerURL) {
    public static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getGenre().getValue(),
                user.getBio(),
                user.getPhotoURL(),
                user.getBannerURL()
        );
    }
}