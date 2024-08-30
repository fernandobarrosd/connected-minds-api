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
import com.fernando.connected_minds_api.services.LikePostService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/like-posts")
@RequiredArgsConstructor
public class LikePostController {
    private final LikePostService likePostService;

    @DeleteMapping("/{likeID}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteLike(
        @AuthenticationPrincipal User user,
        @PathVariable UUID likeID) {
            likePostService.deleteLike(user.getId(), likeID);
        }
}