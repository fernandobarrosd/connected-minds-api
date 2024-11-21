package com.fernando.connected_minds_api.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Optional;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.fernando.connected_minds_api.models.Tag;
import com.fernando.connected_minds_api.repositories.TagRepository;
import com.fernando.connected_minds_api.requests.TagRequest;
import com.fernando.connected_minds_api.responses.TagResponse;

@ExtendWith(MockitoExtension.class)
public class TagServiceTest {
    @InjectMocks
    private TagService tagService;

    @Mock
    private TagRepository tagRepository;


    @Test
    public void shouldReturnListOfTagResponseWhenCallFindAllTagsMethod() {
        List<UUID> ids = List.of(
            UUID.randomUUID(),
            UUID.randomUUID(),
            UUID.randomUUID(),
            UUID.randomUUID()
        );
        List<Tag> tags = List.of(
            new Tag(ids.get(0),"Tag1"),
            new Tag(ids.get(1),"Tag2"),
            new Tag(ids.get(2),"Tag3"),
            new Tag(ids.get(3),"Tag4")
        );

        when(tagRepository.findAll()).thenReturn(tags);

        List<TagResponse> tagResponses = tagService.findAllTags();


        assertEquals(4, tagResponses.size());

        assertEquals("Tag1", tagResponses.get(0).name());
        assertEquals("Tag2", tagResponses.get(1).name());
        assertEquals("Tag3", tagResponses.get(2).name());
        assertEquals("Tag4", tagResponses.get(3).name());


        assertEquals(ids.get(0), tagResponses.get(0).id());
        assertEquals(ids.get(1), tagResponses.get(1).id());
        assertEquals(ids.get(2), tagResponses.get(2).id());
        assertEquals(ids.get(3), tagResponses.get(3).id());
    }

    @Test
    public void shouldSaveOrFindAndReturnAllTags() {
        List<UUID> ids = List.of(
            UUID.randomUUID(),
            UUID.randomUUID()
        );

        List<TagRequest> tagsRequest = List.of(
            new TagRequest(ids.get(0), "Tag1"),
            new TagRequest(ids.get(1), "Tag2"),
            new TagRequest(null, "Tag3"),
            new TagRequest(null, "Tag4")
        );

        List<Tag> tagsDatabase = List.of(
            new Tag(ids.get(0), "Tag1"),
            new Tag(ids.get(1), "Tag2")
        );


        when(tagRepository.findByName("Tag1")).thenReturn(Optional.of(tagsDatabase.get(0)));
        when(tagRepository.findByName("Tag2")).thenReturn(Optional.of(tagsDatabase.get(1)));
        when(tagRepository.findByName("Tag3")).thenReturn(Optional.empty());
        when(tagRepository.findByName("Tag4")).thenReturn(Optional.empty());

        List<Tag> tags = tagService.saveOrFindAndReturnAllTags(tagsRequest);

        assertEquals(4, tags.size());
        assertEquals("Tag1", tags.get(0).getName());
        assertEquals("Tag2", tags.get(1).getName());
        assertEquals("Tag3", tags.get(2).getName());
        assertEquals("Tag4", tags.get(3).getName());

        verify(tagRepository, times(4)).findByName(any(String.class));
        verify(tagRepository, times(2)).save(any(Tag.class));

    }
}