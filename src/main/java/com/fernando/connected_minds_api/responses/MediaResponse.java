package com.fernando.connected_minds_api.responses;

import com.fernando.connected_minds_api.enums.MediaType;
import com.fernando.connected_minds_api.models.Media;

public record MediaResponse(String name,MediaType type) {

    public static MediaResponse toResponse(Media media) {
        return new MediaResponse(
            media.getName(),
            media.getType()
        );
    }
}