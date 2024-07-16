package com.fernando.connected_minds_api.services;

import com.fernando.connected_minds_api.repositories.TagRepository;
import com.fernando.connected_minds_api.responses.TagResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    public List<TagResponse> findAllTags() {
        return tagRepository.findAll()
                .stream()
                .map(TagResponse::fromEntity)
                .toList();
    }
}