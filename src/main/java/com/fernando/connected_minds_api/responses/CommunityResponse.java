package com.fernando.connected_minds_api.responses;

import com.fernando.connected_minds_api.models.Community;
import com.fernando.connected_minds_api.models.Tag;
import com.fernando.connected_minds_api.models.User;
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
        Long postCount,
        Long adminsCount,
        List<String> tags) {
    public static CommunityResponse toResponse(Community community, List<String> tags) {
        return CommunityResponse.builder()
                .id(community.getId())
                .name(community.getName())
                .description(community.getDescription())
                .photoURL(community.getPhotoURL())
                .bannerURL(community.getBannerURL())
                .createdAt(community.getCreatedAt().toString())
                .membersCount((long) community.getMembers().size())
                .groupsCount((long) community.getGroups().size())
                .postCount((long) community.getPosts().size())
                .adminsCount((long) community.getAdmins().size())
                .tags(tags)
                .build();
    }

    public static CommunityResponse toResponse(Community community) {
        return CommunityResponse.builder()
                .id(community.getId())
                .name(community.getName())
                .description(community.getDescription())
                .photoURL(community.getPhotoURL())
                .bannerURL(community.getBannerURL())
                .createdAt(community.getCreatedAt().toString())
                .membersCount((long) community.getMembers().size())
                .groupsCount((long) community.getGroups().size())
                .postCount((long) community.getPosts().size())
                .adminsCount((long) community.getAdmins().size())
                .tags(community.getTags().stream().map(Tag::getName).toList())
                .build();
    }
}