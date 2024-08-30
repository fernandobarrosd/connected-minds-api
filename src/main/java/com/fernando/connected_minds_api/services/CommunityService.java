package com.fernando.connected_minds_api.services;

import com.fernando.connected_minds_api.exceptions.EntityNotFoundException;
import com.fernando.connected_minds_api.exceptions.UserIsAlreadyExiststInCommunityOrGroupException;
import com.fernando.connected_minds_api.models.Community;
import com.fernando.connected_minds_api.models.Group;
import com.fernando.connected_minds_api.models.Tag;
import com.fernando.connected_minds_api.models.User;
import com.fernando.connected_minds_api.repositories.CommunityRepository;
import com.fernando.connected_minds_api.repositories.GroupRepository;
import com.fernando.connected_minds_api.requests.CommunityRequest;
import com.fernando.connected_minds_api.requests.GroupRequest;
import com.fernando.connected_minds_api.responses.CommunityResponse;
import com.fernando.connected_minds_api.responses.GroupResponse;
import com.fernando.connected_minds_api.responses.TagResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.fernando.connected_minds_api.requests.params.PaginationQueryParams;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

@Service
@RequiredArgsConstructor
public class CommunityService {
   private final CommunityRepository communityRepository;
   private final GroupRepository groupRepository;
   private final TagService tagService;

   @Transactional
   public CommunityResponse createCommunity(CommunityRequest communityRequest, User owner) {
        List<Tag> tags = tagService.saveAllTags(communityRequest.tags());

        Community community = new Community(
                communityRequest.name(),
                communityRequest.description(),
                communityRequest.photoURL(),
                communityRequest.bannerURL(),
                owner,
                tags);

        communityRepository.save(community);

        Community communitySaved = communityRepository.findById(community.getId()).get();

        List<TagResponse> tagsResponse = communitySaved.getTags().stream()
                .map(TagResponse::toResponse)
                .toList();

        return CommunityResponse.toResponse(communitySaved, tagsResponse);
   }
   
   public GroupResponse createGroup(User user, GroupRequest groupRequest, UUID communityID) {
        List<Tag> tags = tagService.saveAllTags(groupRequest.tags());

        Community community = communityRepository.findById(communityID)
                .orElseThrow(() -> new EntityNotFoundException("Community is not exists"));
        
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
        return GroupResponse.toResponse(group, tagsResponse, 0L);
   }

   public List<GroupResponse> findAllGroups(UUID communityID, PaginationQueryParams pagination) {
        if (!communityRepository.existsById(communityID)) {
                throw new EntityNotFoundException("Community is not exists"); 
        }


        Pageable pageable = PageRequest.of(pagination.getPage(), pagination.getItemsPerPage());
        List<Group> groups = groupRepository.findAllByCommunityId(communityID, pageable);
        

        return groups.stream()
                .map(group -> GroupResponse.toResponse(group, group.getTags().stream().map(TagResponse::toResponse).toList(), 0L))
                .toList();

   }

   public void addMemberOnCommunity(UUID communityID, User member) {
        Community community = communityRepository.findById(communityID)
                .orElseThrow(() -> new EntityNotFoundException("Community is not exists"));

        
        Optional<User> user = community
                .getMembers()
                .stream().
                filter(userMember -> userMember.getId() == member.getId())
                .findFirst();

        if (user.isPresent()) {
                throw new UserIsAlreadyExiststInCommunityOrGroupException("User is already exists in community#%d".formatted(communityID));               
        }

        community.getMembers().add(member);

        communityRepository.save(community);
   }

   public boolean communityIsExists(UUID communityID) {
        return communityRepository.existsById(communityID);
    }

}