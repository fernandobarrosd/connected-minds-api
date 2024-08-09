package com.fernando.connected_minds_api.responses;

import java.util.UUID;

import com.fernando.connected_minds_api.models.Tag;
import lombok.Builder;

@Builder
public record TagResponse(UUID id, String name) {

    public static TagResponse toResponse(Tag tag) {
        return new TagResponse(tag.getId(), tag.getName());
    }
}