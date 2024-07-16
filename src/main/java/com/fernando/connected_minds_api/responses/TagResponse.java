package com.fernando.connected_minds_api.responses;

import com.fernando.connected_minds_api.models.Tag;

public record TagResponse(String id, String name) {

    public static TagResponse fromEntity(Tag tag) {
        return new TagResponse(tag.getId().toString(), tag.getName());
    }
}