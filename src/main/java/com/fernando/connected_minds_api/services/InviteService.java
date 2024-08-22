package com.fernando.connected_minds_api.services;

import java.util.UUID;
import org.springframework.stereotype.Service;
import com.fernando.connected_minds_api.exceptions.EntityNotFoundException;
import com.fernando.connected_minds_api.models.Invite;
import com.fernando.connected_minds_api.repositories.CommunityRepository;
import com.fernando.connected_minds_api.repositories.GroupRepository;
import com.fernando.connected_minds_api.repositories.InviteRepository;
import com.fernando.connected_minds_api.responses.InviteResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InviteService {
    private final InviteRepository inviteRepository;
    private final CommunityRepository communityRepository;
    private final GroupRepository groupRepository;


    public InviteResponse createInvite(UUID fromID) {
        Boolean fromIDIsNotExists = false;

        if (!communityRepository.existsById(fromID)) {
            fromIDIsNotExists = true;
        }
        else if (!groupRepository.existsById(fromID)) {
            fromIDIsNotExists = true;
        }

        if (fromIDIsNotExists) {
            throw new EntityNotFoundException("Community or group is not exists");
        }

        Invite invite = new Invite(fromID);

        inviteRepository.save(invite);

        return InviteResponse.toResponse(invite);
    }
}