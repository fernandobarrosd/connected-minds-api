package com.fernando.connected_minds_api.controllers;

import com.fernando.connected_minds_api.docs.PostControllerDocumentation;
import com.fernando.connected_minds_api.models.LikePost;
import com.fernando.connected_minds_api.models.User;
import com.fernando.connected_minds_api.queryparams.FindAllPostsQueryParams;
import com.fernando.connected_minds_api.queryparams.PaginationQueryParams;
import com.fernando.connected_minds_api.requests.CommentRequest;
import com.fernando.connected_minds_api.requests.PostRequest;
import com.fernando.connected_minds_api.requests.UpdatePostRequest;
import com.fernando.connected_minds_api.responses.CommentResponse;
import com.fernando.connected_minds_api.responses.PostResponse;
import com.fernando.connected_minds_api.responses.pagination.PaginationResponse;
import com.fernando.connected_minds_api.responses.CreatePostResponse;
import com.fernando.connected_minds_api.services.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController implements PostControllerDocumentation {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<CreatePostResponse> createPost(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid PostRequest postRequest) {
        return ResponseEntity.created(null).body(postService.createPost(postRequest, user));
    }

    @GetMapping
    public ResponseEntity<PaginationResponse<PostResponse>> findAllPosts(
        @Valid FindAllPostsQueryParams queryParams) {
        return ResponseEntity.ok(postService.findAllPosts(queryParams));
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
    public ResponseEntity<PaginationResponse<CommentResponse>> findAllComments(
            @PathVariable UUID postID,
            @Valid PaginationQueryParams queryParams) {

        return ResponseEntity.ok(postService.findAllComments(postID, queryParams));
    }


    @PostMapping("/{postID}/likes")
    public ResponseEntity<LikePost> createLike(
        @AuthenticationPrincipal User user,
        @PathVariable UUID postID) {

            return ResponseEntity.created(null).body(postService.createLike(user, postID));
        }
}