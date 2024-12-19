package com.fernando.connected_minds_api.controllers;

import com.fernando.connected_minds_api.models.User;
import com.fernando.connected_minds_api.queryparams.PaginationQueryParams;
import com.fernando.connected_minds_api.responses.CommunityResponse;
import com.fernando.connected_minds_api.responses.UserResponse;
import com.fernando.connected_minds_api.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{username}")
    public ResponseEntity<UserResponse> findUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.findUserByUsername(username));
    }

    @GetMapping("/communities")
    public ResponseEntity<List<CommunityResponse>> findAllUserCommunities(
            @AuthenticationPrincipal User user,
            @Valid PaginationQueryParams pagination) {
        return ResponseEntity.ok(
                userService.findAllUserCommunities(user.getId(), pagination));
    }
}