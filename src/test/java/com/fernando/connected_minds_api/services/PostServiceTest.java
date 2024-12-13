package com.fernando.connected_minds_api.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import com.fernando.connected_minds_api.repositories.PostRepository;
import net.datafaker.Faker;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PostServiceTest {
    @InjectMocks
    private PostService postService;

    @Mock
    private CommunityService communityService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private GroupService groupService;

    private Faker faker;


    @BeforeEach
    private void setup() {
        this.faker = new Faker();

    }


    @Test
    public void shouldCreateCommunityPost() {
        
           
    }

    @Test
    public void shouldCreateGroupPost() {
        
           
    }

    @Test
    public void shouldFindPostByID() {
        
    }


    @Test
    public void shouldThrowExceptionWhenFindPostByID() {
        
    }


    @Test
    public void shouldDeletePost() {
        
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionWhenDeletePost() {
        
    }


    @Test
    public void shouldThrowsUserIsNotOwnerOfResourceExceptionWhenDeletePost() {
        
    }



}