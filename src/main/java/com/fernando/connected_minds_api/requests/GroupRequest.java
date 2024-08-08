package com.fernando.connected_minds_api.requests;

import java.util.List;

public record GroupRequest(
    String name,
    String description,
    String photoURL,
    String bannerURL,
    List<TagRequest> tags) {}