package com.fernando.connected_minds_api.controllers;

import com.fernando.connected_minds_api.responses.CommentResponse;
import com.fernando.connected_minds_api.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
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
}