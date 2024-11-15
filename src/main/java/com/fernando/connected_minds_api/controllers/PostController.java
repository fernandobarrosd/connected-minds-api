package com.fernando.connected_minds_api.controllers;

import com.fernando.connected_minds_api.models.User;
import com.fernando.connected_minds_api.requests.CommentRequest;
import com.fernando.connected_minds_api.requests.FindAllPostsRequest;
import com.fernando.connected_minds_api.requests.PostRequest;
import com.fernando.connected_minds_api.requests.UpdatePostRequest;
import com.fernando.connected_minds_api.requests.params.PaginationQueryParams;
import com.fernando.connected_minds_api.responses.CommentResponse;
import com.fernando.connected_minds_api.responses.PostResponse;
import com.fernando.connected_minds_api.services.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid PostRequest postRequest) {
        return ResponseEntity.created(null).body(postService.createPost(postRequest, user));
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> findAllPosts(@Valid @RequestBody FindAllPostsRequest request) {
        return ResponseEntity.ok(postService.findAllPosts(request));
    }

    @GetMapping("/{postID}")
    public ResponseEntity<PostResponse> findPostByID(@PathVariable UUID postID) {
        return ResponseEntity.ok(postService.findPostByID(postID));
    }

    @PatchMapping("/{postID}")
    public ResponseEntity<PostResponse> updatePost(
            @AuthenticationPrincipal User user,
            @PathVariable UUID postID,
            @RequestBody @Valid UpdatePostRequest postRequest) {
        return ResponseEntity.ok(postService.updatePost(postID, user.getId(), postRequest));
    }

    @DeleteMapping("/{postID}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(
        @AuthenticationPrincipal User user,
        @PathVariable UUID postID) {
            postService.deletePost(postID, user.getId());
    }

    @PostMapping("/{postID}/comments")
    public ResponseEntity<CommentResponse> createComment(
            @AuthenticationPrincipal User user,
            @PathVariable UUID postID,
            @RequestBody @Valid CommentRequest commentRequest) {

        return ResponseEntity.created(null).body(postService.createComment(user, postID, commentRequest));
    }

    @GetMapping("/{postID}/comments")
    public ResponseEntity<List<CommentResponse>> findAllComments(
            @PathVariable UUID postID,
            @Valid PaginationQueryParams pagination) {

        return ResponseEntity.ok(postService.findAllComments(postID, pagination));
    }


    @PostMapping("/{postID}/likes")
    public ResponseEntity<?> createLike(
        @AuthenticationPrincipal User user,
        @PathVariable UUID postID) {

            return ResponseEntity.created(null).body(postService.createLike(user, postID));
        }
}