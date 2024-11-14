package com.fernando.connected_minds_api.responses;
import java.util.List;
import java.util.UUID;
import com.fernando.connected_minds_api.models.Group;
import lombok.Builder;

@Builder
public record GroupResponse(
    UUID id,
    String name,
    String description,
    String photoURL,
    String bannerURL,
    String createdAt,
    Long membersCount,
    Long postsCount,
    List<TagResponse> tags,
    String communityName) {

    public static GroupResponse toResponse(Group group, List<TagResponse> tags) {
        return GroupResponse.builder()
            .id(group.getId())
            .name(group.getName())
            .description(group.getDescription())
            .photoURL(group.getPhotoURL())
            .bannerURL(group.getBannerURL())
            .createdAt(group.getCreatedAt().toString())
            .tags(tags)
            .membersCount(group.getMembers().stream().count())
            .postsCount(group.getPosts().stream().count())
            .communityName(group.getCommunity().getName())
            .build();
    }
}