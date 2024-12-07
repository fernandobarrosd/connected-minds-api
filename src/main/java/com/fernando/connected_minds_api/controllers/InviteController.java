package com.fernando.connected_minds_api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fernando.connected_minds_api.requests.InviteRequest;
import com.fernando.connected_minds_api.responses.InviteResponse;
import com.fernando.connected_minds_api.services.InviteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.fernando.connected_minds_api.documentation.InviteControllerDocumentation;
import com.fernando.connected_minds_api.models.User;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.UUID;

@RestController
@RequestMapping("/invites")
@RequiredArgsConstructor
public class InviteController implements InviteControllerDocumentation {
    private final InviteService inviteService;

    @PostMapping
    public ResponseEntity<InviteResponse> createInvite(@RequestBody @Valid InviteRequest inviteRequest) {
        return ResponseEntity.created(null).body(inviteService.createInvite(inviteRequest));
    }

    @GetMapping("/{inviteID}/accept")
    public ResponseEntity<InviteResponse> acceptInvite(
        @AuthenticationPrincipal User user,
        @PathVariable UUID inviteID) {
            return ResponseEntity.ok().body(inviteService.acceptInvite(user, inviteID));
        }
}