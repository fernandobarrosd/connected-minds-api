package com.fernando.connected_minds_api.responses;

import com.fernando.connected_minds_api.models.Community;
import lombok.Builder;
import java.util.List;
import java.util.UUID;

@Builder
public record CommunityResponse(
        UUID id,
        String name,
        String description,
        String photoURL,
        String bannerURL,
        String createdAt,
        Long membersCount,
        Long groupsCount,
        Long postsCount,
        List<TagResponse> tags) {
    public static CommunityResponse toResponse(Community community) {
        return CommunityResponse.builder()
                .id(community.getId())
                .name(community.getName())
                .description(community.getDescription())
                .photoURL(community.getPhotoURL())
                .bannerURL(community.getBannerURL())
                .createdAt(community.getCreatedAt().toString())
                .membersCount(community.getMembers().stream().count())
                .groupsCount(community.getGroups().stream().count())
                .postsCount(community.getPosts().stream().count())
                .tags(community.getTags().stream().map(TagResponse::toResponse).toList())
                .build();
    }
}