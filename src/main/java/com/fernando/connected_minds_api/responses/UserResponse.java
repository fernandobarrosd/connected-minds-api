package com.fernando.connected_minds_api.responses;

import com.fernando.connected_minds_api.enums.UserStatus;
import com.fernando.connected_minds_api.models.User;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String username,
        String genre,
        Boolean isOnline,
        String bio,
        String photoURL,
        String bannerURL) {
    public static UserResponse toResponse(User user) {
        Boolean isOnline = user.getStatus() == UserStatus.ONLINE;

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getGenre().getValue(),
                isOnline,
                user.getBio(),
                user.getPhotoURL(),
                user.getBannerURL()
        );
    }
}