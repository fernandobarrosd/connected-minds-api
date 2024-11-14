package com.fernando.connected_minds_api.services;

import com.fernando.connected_minds_api.models.Tag;
import com.fernando.connected_minds_api.repositories.TagRepository;
import com.fernando.connected_minds_api.requests.TagRequest;
import com.fernando.connected_minds_api.responses.TagResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public List<Tag> saveOrFindAndReturnAllTags(List<TagRequest> tagsRequest) {
        List<Tag> tags = new ArrayList<>();

        for (TagRequest tagRequest : tagsRequest) {
            Optional<Tag> tagOptional = tagRepository.findByName(tagRequest.name());

            if (tagOptional.isPresent()) {
                tags.add(tagOptional.get());
            }
            else {
                Tag tag = new Tag(tagRequest.name());
                tagRepository.save(tag);

                tags.add(tag);
            }
            
        }
        return tags;
    }
}