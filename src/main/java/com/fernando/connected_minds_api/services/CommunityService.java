package com.fernando.connected_minds_api.services;

import com.fernando.connected_minds_api.models.Community;
import com.fernando.connected_minds_api.models.Tag;
import com.fernando.connected_minds_api.models.User;
import com.fernando.connected_minds_api.repositories.CommunityRepository;
import com.fernando.connected_minds_api.requests.CommunityRequest;
import com.fernando.connected_minds_api.responses.CommunityResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommunityService {
   private final CommunityRepository communityRepository;

   public CommunityResponse createCommunity(CommunityRequest communityRequest, User owner) {
      List<Tag> tags = communityRequest.tags()
              .stream()
              .map(Tag::new)
              .toList();


      Community community = new Community(
              communityRequest.name(),
              communityRequest.description(),
              communityRequest.photoURL(),
              communityRequest.bannerURL(),
              owner,
              tags);

      communityRepository.save(community);

      return CommunityResponse.toResponse(community, owner, communityRequest.tags());
   }
}
