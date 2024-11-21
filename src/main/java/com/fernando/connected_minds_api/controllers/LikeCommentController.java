package com.fernando.connected_minds_api.controllers;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.fernando.connected_minds_api.models.User;
import com.fernando.connected_minds_api.services.LikeCommentService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/like-comments")
@RequiredArgsConstructor
public class LikeCommentController {
    private final LikeCommentService likeCommentService;


    @DeleteMapping("/{likeID}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLike(
        @AuthenticationPrincipal User user,
        @PathVariable UUID likeID) {
            likeCommentService.deleteLike(user.getId(), likeID);
        }
}