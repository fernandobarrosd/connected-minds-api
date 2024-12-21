package com.fernando.connected_minds_api.responses;

import java.util.UUID;

public record AuthResponse(
    String token, 
    String refreshToken, 
    String tokenExpiresAt,
    UUID id,
    String username,
    String photoURL,
    String bannerURL,
    String bio) {}