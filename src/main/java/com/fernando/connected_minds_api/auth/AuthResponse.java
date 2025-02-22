package com.fernando.connected_minds_api.auth;

import java.util.UUID;

import com.fernando.connected_minds_api.user.UserGenre;
import lombok.Builder;

@Builder
public record AuthResponse(
        String token,
        String refreshToken,
        String tokenExpiresAt,
        UUID userId,
        String username,
        String photoURL,
        String bio,
        UserGenre userGenre) {}