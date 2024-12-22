package com.fernando.connected_minds_api.services;

import com.fernando.connected_minds_api.exceptions.EntityNotFoundException;
import com.fernando.connected_minds_api.exceptions.UserIsAlreadyExiststInCommunityOrGroupException;
import com.fernando.connected_minds_api.managers.CacheManager;
import com.fernando.connected_minds_api.models.Community;
import com.fernando.connected_minds_api.models.Tag;
import com.fernando.connected_minds_api.models.User;
import com.fernando.connected_minds_api.queryparams.PaginationQueryParams;
import com.fernando.connected_minds_api.repositories.CommunityRepository;
import com.fernando.connected_minds_api.requests.CommunityRequest;
import com.fernando.connected_minds_api.requests.GroupRequest;
import com.fernando.connected_minds_api.responses.CommunityResponse;
import com.fernando.connected_minds_api.responses.GroupResponse;
import com.fernando.connected_minds_api.responses.pagination.PaginationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;

@Service
@RequiredArgsConstructor
public class CommunityService {
        private final CommunityRepository communityRepository;
        private final GroupService groupService;
        private final TagService tagService;
        private final CacheManager cacheManager;

        @Transactional
        public CommunityResponse createCommunity(CommunityRequest communityRequest, User owner) {
                List<Tag> tags = tagService.saveOrFindAndReturnAllTags(communityRequest.tags());

                Community community = new Community(
                                communityRequest.name(),
                                communityRequest.description(),
                                communityRequest.photoURL(),
                                communityRequest.bannerURL(),
                                owner,
                                tags);

                communityRepository.save(community);

                return CommunityResponse.toResponse(community);
        }

        public void existsCommunityById(UUID communityID) {
                if (!communityRepository.existsById(communityID)) {
                        throw new EntityNotFoundException("Community or group is not exists");
                }
        }

        public Community findCommunityByID(UUID communityID) {
                String communityCacheKey = "COMMUNITY_ID_%d".formatted(communityID);
                Community communityCache = cacheManager.getCacheValue(communityCacheKey);

                if (cacheManager.hasCacheKey(communityID.toString())) {
                        return communityCache;
                }
                Community community = communityRepository.findById(communityID)
                                .orElseThrow(() -> new EntityNotFoundException("Community is not exists"));
        
                cacheManager.addCacheValue(communityCacheKey, community);
                return community;

        }

        public GroupResponse createGroup(User user, GroupRequest groupRequest, UUID communityID) {
                List<Tag> tags = tagService.saveOrFindAndReturnAllTags(groupRequest.tags());

                Community community = findCommunityByID(communityID);

                GroupResponse groupResponse = groupService.saveGroup(groupRequest, user, community, tags);
                return groupResponse;
        }

        public List<GroupResponse> findAllGroups(UUID communityID, PaginationQueryParams pagination) {
                if (!communityRepository.existsById(communityID)) {
                        throw new EntityNotFoundException("Community is not exists");
                }

                Pageable pageable = PageRequest.of(pagination.getPage(), pagination.getItemsPerPage());
                return groupService.findAllGroups(communityID, pageable);

        }

        public void addMemberOnCommunity(UUID communityID, User member) {
                Community community = communityRepository.findById(communityID)
                                .orElseThrow(() -> new EntityNotFoundException("Community is not exists"));

                Optional<User> user = community
                                .getMembers()
                                .stream().filter(userMember -> userMember.getId() == member.getId())
                                .findFirst();

                if (user.isPresent()) {
                        throw new UserIsAlreadyExiststInCommunityOrGroupException(
                                        "User is already exists in community#%d".formatted(communityID));
                }

                community.getMembers().add(member);

                communityRepository.save(community);
        }

        public boolean communityIsExists(UUID communityID) {
                return communityRepository.existsById(communityID);
        }

        public PaginationResponse<CommunityResponse> searchCommunity(String search, Integer page,
                        Integer itemsPerPage) {
                String cacheCommunitiesKey = "SEARCHED_COMMUNITIES_%s_PAGE_%d".formatted(
                                search.replaceAll(" ", "_"),
                                page);

                Page<Community> cacheCommunityPage = cacheManager.getCacheValue(cacheCommunitiesKey);

                if (cacheCommunityPage != null) {
                        List<CommunityResponse> users = cacheCommunityPage.get()
                                        .map(CommunityResponse::toResponse)
                                        .toList();

                        return PaginationResponse.toResponse(
                                cacheCommunityPage.hasNext(),
                                cacheCommunityPage.getTotalPages(),
                                cacheCommunityPage.getTotalElements(),
                                itemsPerPage,
                                page,
                                users);
                }

                Pageable pageable = PageRequest.of(page, itemsPerPage);
                Page<Community> communityPage = communityRepository.findAllByNameContaining(search, pageable);

                List<CommunityResponse> communities = communityPage.get()
                                .map(CommunityResponse::toResponse)
                                .toList();

                return PaginationResponse.toResponse(
                                communityPage.hasNext(),
                                communityPage.getTotalPages(),
                                communityPage.getTotalElements(),
                                itemsPerPage,
                                page,
                                communities);
        }
}