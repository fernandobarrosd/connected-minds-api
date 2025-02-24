package com.fernando.connected_minds_api.avatar;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/avatars")
@RequiredArgsConstructor
public class AvatarController {
    private final AvatarService avatarService;

    @GetMapping(value = "/{avatarFileName}", produces = {"image/png", "application/json"})
    public ResponseEntity<Resource> findAvatarFileByName(
                @PathVariable String avatarFileName) throws Exception {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(avatarService.findAvatarFileByName(avatarFileName));
    }
}