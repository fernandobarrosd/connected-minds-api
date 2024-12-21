package com.fernando.connected_minds_api.controllers;

import com.fernando.connected_minds_api.docs.CommunityControllerDocumentation;
import com.fernando.connected_minds_api.models.User;
import com.fernando.connected_minds_api.queryparams.PaginationQueryParams;
import com.fernando.connected_minds_api.requests.CommunityRequest;
import com.fernando.connected_minds_api.requests.GroupRequest;
import com.fernando.connected_minds_api.responses.CommunityResponse;
import com.fernando.connected_minds_api.responses.GroupResponse;
import com.fernando.connected_minds_api.services.CommunityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/communities")
@RequiredArgsConstructor
public class CommunityController implements CommunityControllerDocumentation {
    private final CommunityService communityService;

    @PostMapping
    public ResponseEntity<CommunityResponse> createCommunity(
            @AuthenticationPrincipal User owner,
            @RequestBody @Valid CommunityRequest communityRequest) {
        return ResponseEntity.created(null).body(
                communityService.createCommunity(communityRequest, owner));
    }

    @PostMapping("/{communityID}/groups")
    public ResponseEntity<GroupResponse> createGroup(
        @AuthenticationPrincipal User user,
        @RequestBody @Valid GroupRequest groupRequest,
        @PathVariable @Valid UUID communityID) {
            return ResponseEntity.created(null).body(
                communityService.createGroup(user, groupRequest, communityID));

        }
    @GetMapping("/{communityID}/groups")
    public ResponseEntity<List<GroupResponse>> findAllGroups(
        @PathVariable @Valid String communityID,
        @Valid PaginationQueryParams pagination) {
        return ResponseEntity.ok(communityService.findAllGroups(UUID.fromString(communityID), pagination));
    }
}