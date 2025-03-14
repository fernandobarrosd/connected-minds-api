package com.fernando.connected_minds_api.user;

import java.util.UUID;

public record UserResponse(
        UUID userID,
        String username,
        String birthDate,
        String photoURL,
        String bannerURL,
        String bio,
        UserGenre genre) {}