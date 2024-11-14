package com.fernando.connected_minds_api.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.fernando.connected_minds_api.exceptions.EntityNotFoundException;
import com.fernando.connected_minds_api.exceptions.UserIsAlreadyExiststInCommunityOrGroupException;
import com.fernando.connected_minds_api.models.Community;
import com.fernando.connected_minds_api.models.Group;
import com.fernando.connected_minds_api.models.Tag;
import com.fernando.connected_minds_api.models.User;
import com.fernando.connected_minds_api.repositories.GroupRepository;
import com.fernando.connected_minds_api.requests.GroupRequest;
import com.fernando.connected_minds_api.responses.GroupResponse;
import com.fernando.connected_minds_api.responses.TagResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;


    public GroupResponse saveGroup(GroupRequest groupRequest, User user, Community community, List<Tag> tags) {
        Group group = new Group(
                groupRequest.name(),
                groupRequest.description(),
                groupRequest.photoURL(),
                groupRequest.bannerURL(),
                user,
                community,
                tags
        );

        groupRepository.save(group);

        List<TagResponse> tagsResponse = !tags.isEmpty() ? tags.stream()
                .map(TagResponse::toResponse)
                .toList()
        : List.of();
        return GroupResponse.toResponse(group, tagsResponse);
    }

    public void existsGroupById(UUID groupID) {
        if (!groupRepository.existsById(groupID)) {
                throw new EntityNotFoundException("Community or group is not exists");
        }
   }

    public List<GroupResponse> findAllGroups(UUID communityID, Pageable pageable) {
        List<Group> groups = groupRepository.findAllByCommunityId(communityID, pageable);
        

        return groups.stream()
                .map(group -> GroupResponse.toResponse(group, group.getTags().stream().map(TagResponse::toResponse).toList()))
                .toList();
    }

    public void addMemberOnGroup(UUID groupID, User member) {
        Group group = groupRepository.findById(groupID)
                .orElseThrow(() -> new EntityNotFoundException("Group is not exists"));
        
        Optional<User> user = group
                .getMembers()
                .stream().
                filter(userMember -> userMember.getId() == member.getId())
                .findFirst();

        if (user.isPresent()) {
                throw new UserIsAlreadyExiststInCommunityOrGroupException("User is already exists in group#%d".formatted(groupID));               
        }
        
        group.getMembers().add(member);

        groupRepository.save(group);
    }

    public boolean groupIsExists(UUID groupID) {
        return groupRepository.existsById(groupID);
    }
}
