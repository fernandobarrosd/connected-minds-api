package com.fernando.connected_minds_api.auth;

import java.util.UUID;
import lombok.Builder;

@Builder
public record AuthResponse(
    String token, 
    String refreshToken, 
    String tokenExpiresAt,
    UUID userId,
    String username,
    String photoURL,
    String bannerURL,
    String bio) {}