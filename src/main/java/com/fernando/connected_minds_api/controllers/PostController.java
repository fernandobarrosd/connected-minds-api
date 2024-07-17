package com.fernando.connected_minds_api.controllers;

import com.fernando.connected_minds_api.models.User;
import com.fernando.connected_minds_api.requests.PostRequest;
import com.fernando.connected_minds_api.responses.PostResponse;
import com.fernando.connected_minds_api.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            @AuthenticationPrincipal User user,
            @RequestBody PostRequest postRequest) {
        return ResponseEntity.created(null).body(postService.createPost(postRequest, user));
    }

    @GetMapping("/{postID}")
    public ResponseEntity<PostResponse> findPostByID(@PathVariable UUID postID) {
        return ResponseEntity.ok(postService.findPostByID(postID));
    }
}
