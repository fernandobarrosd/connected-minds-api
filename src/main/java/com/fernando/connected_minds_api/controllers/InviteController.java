package com.fernando.connected_minds_api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fernando.connected_minds_api.requests.InviteRequest;
import com.fernando.connected_minds_api.responses.InviteResponse;
import com.fernando.connected_minds_api.services.InviteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/invites")
@RequiredArgsConstructor
public class InviteController {
    private final InviteService inviteService;

    @PostMapping
    public ResponseEntity<InviteResponse> createInvite(@RequestBody @Valid InviteRequest inviteRequest) {
        return ResponseEntity.created(null).body(inviteService.createInvite(inviteRequest.fromID()));
    }
}