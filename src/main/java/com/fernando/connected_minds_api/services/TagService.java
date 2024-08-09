package com.fernando.connected_minds_api.services;

import com.fernando.connected_minds_api.models.Tag;
import com.fernando.connected_minds_api.repositories.TagRepository;
import com.fernando.connected_minds_api.requests.TagRequest;
import com.fernando.connected_minds_api.responses.TagResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    public List<TagResponse> findAllTags() {
        return tagRepository.findAll()
                .stream()
                .map(TagResponse::toResponse)
                .toList();
    }

    public List<Tag> saveAllTags(List<TagRequest> tagsRequest) {
        List<Tag> tags = tagsRequest
                .stream()
                .map(tag -> {
                        if (tag.id() == null) {
                                System.out.println(tag.id());
                                return new Tag(tag.name());
                        }
                        return new Tag(UUID.fromString(tag.id()), tag.name());
                })
                .toList();
        
        tagRepository.saveAll(tags);
        return tags;
    }
}