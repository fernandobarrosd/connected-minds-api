package com.fernando.connected_minds_api.controllers;


import com.fernando.connected_minds_api.responses.TagResponse;
import com.fernando.connected_minds_api.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @GetMapping
    public ResponseEntity<List<TagResponse>> findAllTags() {
        return ResponseEntity.ok(tagService.findAllTags());
    }
}
