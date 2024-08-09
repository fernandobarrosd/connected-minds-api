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
    Long adminsCount,
    List<TagResponse> tags,
    String communityName) {

    public static GroupResponse toResponse(Group group, List<TagResponse> tags, Long adminsCount) {
        return GroupResponse.builder()
            .id(group.getId())
            .name(group.getName())
            .description(group.getDescription())
            .photoURL(group.getPhotoURL())
            .bannerURL(group.getBannerURL())
            .createdAt(group.getCreatedAt().toString())
            .tags(tags)
            .membersCount((long) group.getMembers().size())
            .postsCount((long) group.getPosts().size())
            .adminsCount(adminsCount)
            .communityName(group.getCommunity().getName())
            .build();
    }
}