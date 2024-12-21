package com.fernando.connected_minds_api.controllers;

import com.fernando.connected_minds_api.models.LikeComment;
import com.fernando.connected_minds_api.models.User;
import com.fernando.connected_minds_api.queryparams.PaginationQueryParams;
import com.fernando.connected_minds_api.requests.CommentRequest;
import com.fernando.connected_minds_api.requests.UpdateCommentRequest;
import com.fernando.connected_minds_api.responses.CommentResponse;
import com.fernando.connected_minds_api.responses.pagination.PaginationResponse;
import com.fernando.connected_minds_api.services.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.fernando.connected_minds_api.docs.CommentControllerDocumentation;
import java.util.UUID;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController implements CommentControllerDocumentation {
    private final CommentService commentService;

    @GetMapping("/{commentID}")
    public ResponseEntity<CommentResponse> findCommentById(@PathVariable UUID commentID) {
        return ResponseEntity.ok(commentService.findCommentById(commentID));
    }

    @PatchMapping("/{commentID}")
    public ResponseEntity<CommentResponse> updateComment(
            @AuthenticationPrincipal User owner,
            @PathVariable UUID commentID,
            @RequestBody @Valid UpdateCommentRequest commentRequest) {
        return ResponseEntity.ok(commentService.updateComment(commentID, commentRequest, owner.getId()));
    }


    @DeleteMapping("/{commentID}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(
        @AuthenticationPrincipal User owner,
        @PathVariable UUID commentID) {
        commentService.deleteComment(commentID, owner.getId());
    }

    @PostMapping("/{commentID}/comments")
    public ResponseEntity<CommentResponse> createCommentOfComment(
            @AuthenticationPrincipal User owner,
            @PathVariable UUID commentID,
            @RequestBody @Valid CommentRequest commentRequest) {

        return ResponseEntity.created(null).body(
                commentService.saveCommentOfComment(commentID, commentRequest, owner));
    }


    @GetMapping("/{commentID}/comments")
    public ResponseEntity<PaginationResponse<CommentResponse>> findAllCommentsOfComment(
            @PathVariable UUID commentID,
            @Valid PaginationQueryParams queryParams) {
        return ResponseEntity.ok(commentService.findAllCommentsOfComment(commentID, queryParams));
    }

    @PostMapping("/{commentID}/likes")
    public ResponseEntity<LikeComment> createLike(
        @AuthenticationPrincipal User user,
        @PathVariable UUID commentID) {
            return ResponseEntity.created(null).body(commentService.saveLikeComment(user, commentID));
        }
}