package com.fernando.connected_minds_api.controllers;

import com.fernando.connected_minds_api.models.User;
import com.fernando.connected_minds_api.requests.CommunityRequest;
import com.fernando.connected_minds_api.responses.CommunityResponse;
import com.fernando.connected_minds_api.services.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/communities")
@RequiredArgsConstructor
public class CommunityController {
    private final CommunityService communityService;

    @PostMapping
    public ResponseEntity<CommunityResponse> createCommunity(
            @AuthenticationPrincipal User owner,
            @RequestBody CommunityRequest communityRequest) {
        return ResponseEntity.created(null).body(
                communityService.createCommunity(communityRequest, owner));
    }
}