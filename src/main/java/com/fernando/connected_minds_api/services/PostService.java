package com.fernando.connected_minds_api.services;

import com.fernando.connected_minds_api.exceptions.CannotLikeTheOwnPostException;
import com.fernando.connected_minds_api.exceptions.EntityNotFoundException;
import com.fernando.connected_minds_api.exceptions.UserIsNotOwnerOfResourceException;
import com.fernando.connected_minds_api.models.LikePost;
import com.fernando.connected_minds_api.models.Post;
import com.fernando.connected_minds_api.models.User;
import com.fernando.connected_minds_api.queryparams.FindAllPostsQueryParams;
import com.fernando.connected_minds_api.queryparams.PaginationQueryParams;
import com.fernando.connected_minds_api.models.Community;
import com.fernando.connected_minds_api.models.Group;
import com.fernando.connected_minds_api.repositories.PostRepository;
import com.fernando.connected_minds_api.requests.CommentRequest;
import com.fernando.connected_minds_api.requests.NotificationRequest;
import com.fernando.connected_minds_api.requests.PostRequest;
import com.fernando.connected_minds_api.requests.UpdatePostRequest;
import com.fernando.connected_minds_api.responses.CommentResponse;
import com.fernando.connected_minds_api.responses.NotificationResponse;
import com.fernando.connected_minds_api.responses.PostResponse;
import com.fernando.connected_minds_api.responses.pagination.PaginationResponse;
import com.fernando.connected_minds_api.responses.CreatePostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import com.fernando.connected_minds_api.enums.NotificationType;
import com.fernando.connected_minds_api.enums.PostLocationType;


@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final CommentService commentService;
    private final CommunityService communityService;
    private final GroupService groupService;
    private final LikePostService likePostService;
    private final NotificationService notificationService;

    public CreatePostResponse createPost(PostRequest postRequest, User owner) {
        UUID locationID = postRequest.locationID();
        NotificationType notificationType = NotificationType.POST;
        Map<String, Object> data = new HashMap<>();
        
        
        if (postRequest.locationType() == PostLocationType.COMMUNITY) {
            Community community = communityService.findCommunityByID(locationID);
            data.put("name", community.getName());
            data.put("members", community.getMembers());
        }
        else {
            Group group = groupService.findGroupByID(locationID);
            data.put("name", group.getName());
            data.put("members", group.getMembers());
        }
        
        Post post = new Post(
                postRequest.content(),
                postRequest.photoURL(),
                locationID,
                owner
        );
        postRepository.save(post);

        String content = "%s postou no %s".formatted(owner.getUsername(), data.get("name"));
        List<User> members = (List<User>) data.get("members");
        
        NotificationRequest request = new NotificationRequest(
            post.getId(),
            content,
            null,
            notificationType
        );
        NotificationResponse notificationResponse = notificationService.saveNotification(request, owner);

        List<String> membersUsernames = members.stream()
        .map(member -> member.getUsername()).toList();
        
        return CreatePostResponse.toResponse(post, membersUsernames, notificationResponse.id());
    }

    public PaginationResponse<PostResponse> findAllPosts(FindAllPostsQueryParams queryParams) {
        UUID locationID = queryParams.getLocationID();

        if (queryParams.getLocationType() == PostLocationType.COMMUNITY) {
            communityService.existsCommunityById(locationID);
        }
        else {
            groupService.existsGroupById(locationID);
        }

        Pageable pageable = PageRequest.of(
            queryParams.getPage(),
            queryParams.getItemsPerPage()
        );

        Page<Post> postPage = postRepository.findAllByLocationID(locationID, pageable);


        List<PostResponse> posts = postPage.get()
        .map(PostResponse::toResponse)
        .toList();

        return PaginationResponse.toResponse(
            postPage.hasNext(),
            postPage.getTotalPages(),
            postPage.getTotalElements(),
            queryParams.getItemsPerPage(),
            queryParams.getPage(),
            posts
        );
    }

    public PostResponse findPostByID(UUID postID) {
        Optional<Post> postOptional = postRepository.findById(postID);
        if (postOptional.isEmpty()) {
            throw new EntityNotFoundException("Post is not exists");
        }
        Post post = postOptional.get();
        return PostResponse.toResponse(post);
    }

    public CommentResponse createComment(User user, UUID postID, CommentRequest commentRequest) {
        Post post = postRepository.findById(postID)
                .orElseThrow(() -> new EntityNotFoundException("Post is not exists"));

        CommentResponse commentResponse = commentService.saveComment(commentRequest.content(), user, post);

        return commentResponse;
    }

    public void deletePost(UUID postID, UUID userID) {
        Post post = postRepository.findById(postID)
            .orElseThrow(() -> new EntityNotFoundException("Post is not exists"));
        
        if (post.getOwner().getId() != userID) {
            throw new UserIsNotOwnerOfResourceException("%s is not owner of post#%s".
                formatted(post.getOwner().getUsername(), postID.toString()));
        }
        postRepository.deleteById(postID);
    }

    public PostResponse updatePost(UUID postID, UUID userID, UpdatePostRequest postRequest) {
        Post post = postRepository.findById(postID)
                .orElseThrow(() -> new EntityNotFoundException("Post is not exists"));
        
        if (post.getOwner().getId() != userID) {
            throw new UserIsNotOwnerOfResourceException("%s is not owner of post#%s".
                    formatted(post.getOwner().getUsername(), postID.toString()));
        }

        if (postRequest.content() != null) {
            post.setContent(postRequest.content());
        }
        if (postRequest.photoURL() != null) {
            post.setPhotoURL(postRequest.photoURL());
        }
        postRepository.save(post);
        return PostResponse.toResponse(post);
    }

    public PaginationResponse<CommentResponse> findAllComments(UUID postID, PaginationQueryParams queryParams) {
        if (!postRepository.existsById(postID)) {
            throw new EntityNotFoundException("Post is not exists");
        }

        return commentService.findAllComments(
            queryParams.getPage(),
            queryParams.getItemsPerPage(),
            postID
        );
    }

    public LikePost createLike(User user, UUID postID) {
        Post post = postRepository.findById(postID)
                .orElseThrow(() -> new EntityNotFoundException("Post is not exists"));

        if (post.getOwner().getId() == user.getId()) {
            throw new CannotLikeTheOwnPostException("Actually auth user dont like the own post");
        }

        LikePost like = new LikePost(user, post);

        likePostService.saveLike(like);

        return like;
    }
}