package com.fernando.connected_minds_api.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import com.fernando.connected_minds_api.enums.PostLocationType;
import com.fernando.connected_minds_api.exceptions.EntityNotFoundException;
import com.fernando.connected_minds_api.exceptions.UserIsNotOwnerOfResourceException;
import com.fernando.connected_minds_api.models.Post;
import com.fernando.connected_minds_api.models.User;
import com.fernando.connected_minds_api.repositories.PostRepository;
import com.fernando.connected_minds_api.requests.PostRequest;
import com.fernando.connected_minds_api.responses.OwnerResponse;
import com.fernando.connected_minds_api.responses.PostResponse;
import com.fernando.connected_minds_api.utils.FakerUtils;
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
        User owner = FakerUtils.generateFakeUser(faker);

        PostRequest request = FakerUtils.generateFakePostRequest(
            faker, 
            PostLocationType.COMMUNITY
        );


        doNothing().when(communityService).existsCommunityById(request.locationID());
        
        PostResponse response = postService.createPost(request, owner);
        OwnerResponse ownerResponse = response.owner();
        

        assertNotNull(response);
        assertNotNull(response.content());
        assertNotNull(response.createdAt());
        assertNotNull(response.likes());
        assertNull(response.photoURL());

        assertNotNull(ownerResponse);
        assertNotNull(ownerResponse.id());
        assertNotNull(ownerResponse.username());
        assertNull(ownerResponse.photoURL());

        assertEquals(request.content(), response.content());
        assertEquals(request.locationID(), response.locationID());

        assertEquals(owner.getId(), ownerResponse.id());
        assertEquals(owner.getUsername(), ownerResponse.username());
           
    }

    @Test
    public void shouldCreateGroupPost() {
        User owner = FakerUtils.generateFakeUser(faker);

        PostRequest request = FakerUtils.generateFakePostRequest(
            faker, 
            PostLocationType.GROUP
        );


        doNothing().when(groupService).existsGroupById(request.locationID());
        
        PostResponse response = postService.createPost(request, owner);
        OwnerResponse ownerResponse = response.owner();
        

        assertNotNull(response);
        assertNotNull(response.content());
        assertNotNull(response.createdAt());
        assertNotNull(response.likes());
        assertNull(response.photoURL());

        assertNotNull(ownerResponse);
        assertNotNull(ownerResponse.id());
        assertNotNull(ownerResponse.username());
        assertNull(ownerResponse.photoURL());


        assertEquals(request.content(), response.content());
        assertEquals(request.locationID(), response.locationID());

        assertEquals(owner.getId(), ownerResponse.id());
        assertEquals(owner.getUsername(), ownerResponse.username());
           
    }

    @Test
    public void shouldFindPostByID() {
        User owner = FakerUtils.generateFakeUser(faker);
        UUID postID = UUID.randomUUID();
        Post post = FakerUtils.generateFakePost(faker, owner);

        post.setId(postID);

        when(postRepository.findById(postID)).thenReturn(Optional.of(post));

        PostResponse response = postService.findPostByID(postID);
        OwnerResponse ownerResponse = response.owner();

        assertNotNull(response);
        assertNotNull(response.content());
        assertNotNull(response.createdAt());
        assertNotNull(response.likes());
        assertNull(response.photoURL());

        assertNotNull(ownerResponse);
        assertNotNull(ownerResponse.id());
        assertNotNull(ownerResponse.username());
        assertNull(ownerResponse.photoURL());

        assertEquals(post.getId(), response.id());
        assertEquals(post.getContent(), response.content());
        assertEquals(post.getLocationID(), response.locationID());

        assertEquals(owner.getId(), ownerResponse.id());
        assertEquals(owner.getUsername(), ownerResponse.username());
    }


    @Test
    public void shouldThrowExceptionWhenFindPostByID() {
        when(postRepository.findById(null)).thenReturn(Optional.empty());

        var exception = assertThrows(
            EntityNotFoundException.class,
            () -> postService.findPostByID(null)
        );

        assertNotNull(exception);
        assertNotNull(exception.getMessage());

        assertEquals("Post is not exists", exception.getMessage());
    }


    @Test
    public void shouldDeletePost() {
        UUID postID = UUID.randomUUID();
        User owner = FakerUtils.generateFakeUser(faker);
        Post post = FakerUtils.generateFakePost(faker, owner);

        post.setId(postID);
        

        when(postRepository.findById(postID)).thenReturn(Optional.of(post));

        assertDoesNotThrow(() -> postService.deletePost(postID, owner.getId()));
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionWhenDeletePost() {
        UUID postID = null;
        UUID userID = UUID.randomUUID();

        when(postRepository.findById(postID)).thenReturn(Optional.empty());

        var exception = assertThrows(
            EntityNotFoundException.class,
            () -> postService.deletePost(postID, userID)
        );


        assertNotNull(exception);
        assertNotNull(exception.getMessage());

        assertEquals("Post is not exists", exception.getMessage());
    }


    @Test
    public void shouldThrowsUserIsNotOwnerOfResourceExceptionWhenDeletePost() {
        UUID postID = UUID.randomUUID();
        UUID userID = UUID.randomUUID();
        User owner = FakerUtils.generateFakeUser(faker);

        Post post = FakerUtils.generateFakePost(faker, owner);
        var errorMessage = "%s is not owner of post#%s".formatted(
            post.getOwner().getUsername(),
            postID.toString()
        );


        when(postRepository.findById(postID)).thenReturn(Optional.of(post));

        var exception = assertThrows(
            UserIsNotOwnerOfResourceException.class,
            () -> postService.deletePost(postID, userID)
        );

        assertNotNull(exception);
        assertNotNull(exception.getMessage());

        assertEquals(errorMessage, exception.getMessage());
    }



}