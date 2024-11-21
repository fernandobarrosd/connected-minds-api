package com.fernando.connected_minds_api.services;

import com.fernando.connected_minds_api.exceptions.CannotLikeTheOwnPostException;
import com.fernando.connected_minds_api.exceptions.EntityNotFoundException;
import com.fernando.connected_minds_api.exceptions.UserIsNotOwnerOfResourceException;
import com.fernando.connected_minds_api.models.LikePost;
import com.fernando.connected_minds_api.models.Post;
import com.fernando.connected_minds_api.models.User;
import com.fernando.connected_minds_api.models.Community;
import com.fernando.connected_minds_api.models.Group;
import com.fernando.connected_minds_api.repositories.PostRepository;
import com.fernando.connected_minds_api.requests.CommentRequest;
import com.fernando.connected_minds_api.requests.FindAllPostsRequest;
import com.fernando.connected_minds_api.requests.NotificationRequest;
import com.fernando.connected_minds_api.requests.PostRequest;
import com.fernando.connected_minds_api.requests.UpdatePostRequest;
import com.fernando.connected_minds_api.requests.params.PaginationQueryParams;
import com.fernando.connected_minds_api.responses.CommentResponse;
import com.fernando.connected_minds_api.responses.NotificationResponse;
import com.fernando.connected_minds_api.responses.PostResponse;
import lombok.RequiredArgsConstructor;
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

    public PostResponse createPost(PostRequest postRequest, User owner) {
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


        members.forEach(member -> {
            notificationService.sendNotification(member.getId(), notificationResponse);
        });
        
        return PostResponse.toResponse(post);
    }

    public List<PostResponse> findAllPosts(FindAllPostsRequest request) {
        UUID locationID = request.locationID();

        if (request.locationType() == PostLocationType.COMMUNITY) {
            communityService.existsCommunityById(locationID);
        }
        else {
            groupService.existsGroupById(locationID);
        }

        return postRepository.findAllByLocationID(locationID)
            .stream()
            .map(PostResponse::toResponse)
            .toList();
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

    public List<CommentResponse> findAllComments(UUID postID, PaginationQueryParams pagination) {
        if (!postRepository.existsById(postID)) {
            throw new EntityNotFoundException("Post is not exists");
        }
        return commentService.findAllComments(pagination, postID);
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