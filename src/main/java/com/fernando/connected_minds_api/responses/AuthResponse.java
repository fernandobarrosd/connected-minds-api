package com.fernando.connected_minds_api.responses;

public record AuthResponse(
    String token, 
    String refreshToken, 
    String tokenExpiresAt,
    String username,
    String photoURL,
    String bannerURL,
    String bio) {}