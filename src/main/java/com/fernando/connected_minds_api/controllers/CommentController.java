package com.fernando.connected_minds_api.controllers;

import com.fernando.connected_minds_api.models.User;
import com.fernando.connected_minds_api.requests.CommentRequest;
import com.fernando.connected_minds_api.responses.CommentResponse;
import com.fernando.connected_minds_api.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @DeleteMapping("/{commentID}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable UUID commentID) {
        commentService.deleteComment(commentID);
    }

    @GetMapping("/{commentID}")
    public ResponseEntity<CommentResponse> findCommentById(@PathVariable UUID commentID) {
        return ResponseEntity.ok(commentService.findCommentById(commentID));
    }

    @PostMapping("/{commentID}/comments")
    public ResponseEntity<CommentResponse> createCommentOfComment(
            @AuthenticationPrincipal User owner,
            @PathVariable UUID commentID,
            @RequestBody CommentRequest commentRequest) {

        return ResponseEntity.created(null).body(commentService.createCommentOfComment(commentID, commentRequest, owner));
    }

    @GetMapping("/{commentID}/comments")
    public ResponseEntity<List<CommentResponse>> findAllCommentsOfComment(
            @PathVariable UUID commentID,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "5") Integer itemsPerPage) {
        return ResponseEntity.ok(commentService.findAllCommentsOfComment(commentID, page, itemsPerPage));
    }
}
