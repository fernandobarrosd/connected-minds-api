package com.fernando.connected_minds_api.responses;

import java.util.UUID;

import com.fernando.connected_minds_api.enums.InviteType;
import com.fernando.connected_minds_api.models.Invite;
import lombok.Builder;

@Builder
public record InviteResponse(
    UUID id,
    UUID fromID,
    InviteType inviteType) {

        public static InviteResponse toResponse(Invite invite) {
            return InviteResponse.builder()
                .id(invite.getId())
                .fromID(invite.getFromID())
                .inviteType(invite.getType())
                .build();
        }
    }