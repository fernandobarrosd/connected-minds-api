package com.fernando.connected_minds_api.services;

import java.util.UUID;
import org.springframework.stereotype.Service;
import com.fernando.connected_minds_api.exceptions.EntityNotFoundException;
import com.fernando.connected_minds_api.models.Invite;
import com.fernando.connected_minds_api.repositories.InviteRepository;
import com.fernando.connected_minds_api.requests.InviteRequest;
import com.fernando.connected_minds_api.responses.InviteResponse;
import lombok.RequiredArgsConstructor;
import com.fernando.connected_minds_api.models.User;
import com.fernando.connected_minds_api.enums.InviteType;

@Service
@RequiredArgsConstructor
public class InviteService {
    private final InviteRepository inviteRepository;
    private final CommunityService communityService;
    private final GroupService groupService;


    public InviteResponse createInvite(InviteRequest inviteRequest) {
        if (inviteRequest.type() == InviteType.COMMUNITY) {
            communityService.existsCommunityById(inviteRequest.fromID());
        }
        else {
            groupService.existsGroupById(inviteRequest.fromID());
        }

        Invite invite = new Invite(
            inviteRequest.fromID(), 
            inviteRequest.type()
        );

        inviteRepository.save(invite);

        return InviteResponse.toResponse(invite);
    }


    public InviteResponse acceptInvite(User user, UUID inviteID) {
        Invite invite = inviteRepository.findById(inviteID)
        .orElseThrow(() -> new EntityNotFoundException("Invite is not exists"));

        
        if (invite.getType() == InviteType.COMMUNITY) {
            communityService.addMemberOnCommunity(invite.getFromID(), user);
        }
        else {
           groupService.addMemberOnGroup(invite.getFromID(), user);
        }

        return InviteResponse.toResponse(invite);
    }
}