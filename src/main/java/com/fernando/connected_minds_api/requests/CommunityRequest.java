package com.fernando.connected_minds_api.requests;

import java.util.List;

public record CommunityRequest(
        String name,
        String description,
        List<TagRequest> tags,
        String photoURL,
        String bannerURL) {}