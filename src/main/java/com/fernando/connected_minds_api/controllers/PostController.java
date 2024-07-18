package com.fernando.connected_minds_api.controllers;

import com.fernando.connected_minds_api.models.User;
import com.fernando.connected_minds_api.requests.CommentRequest;
import com.fernando.connected_minds_api.requests.PostRequest;
import com.fernando.connected_minds_api.requests.UpdatePostRequest;
import com.fernando.connected_minds_api.responses.CommentResponse;
import com.fernando.connected_minds_api.responses.PostResponse;
import com.fernando.connected_minds_api.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @DeleteMapping("/{postID}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable UUID postID) {
        postService.deletePost(postID);
    }

    @PatchMapping("/{postID}")
    public ResponseEntity<PostResponse> updatePost(
            @PathVariable UUID postID,
            @RequestBody UpdatePostRequest postRequest) {
        return ResponseEntity.ok(postService.updatePost(postID, postRequest));
    }

    @PostMapping("/{postID}/comments")
    public ResponseEntity<CommentResponse> createComment(
            @AuthenticationPrincipal User user,
            @PathVariable UUID postID,
            @RequestBody CommentRequest commentRequest) {

        return ResponseEntity.created(null).body(postService.createComment(user, postID, commentRequest));
    }
}
