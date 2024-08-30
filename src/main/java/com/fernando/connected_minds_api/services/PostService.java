package com.fernando.connected_minds_api.services;

import com.fernando.connected_minds_api.exceptions.CannotLikeTheOwnPostException;
import com.fernando.connected_minds_api.exceptions.EntityNotFoundException;
import com.fernando.connected_minds_api.exceptions.UserIsNotOwnerOfResourceException;
import com.fernando.connected_minds_api.models.Comment;
import com.fernando.connected_minds_api.models.LikePost;
import com.fernando.connected_minds_api.models.Post;
import com.fernando.connected_minds_api.models.User;
import com.fernando.connected_minds_api.repositories.CommentRepository;
import com.fernando.connected_minds_api.repositories.CommunityRepository;
import com.fernando.connected_minds_api.repositories.PostRepository;
import com.fernando.connected_minds_api.requests.CommentRequest;
import com.fernando.connected_minds_api.requests.PostRequest;
import com.fernando.connected_minds_api.requests.UpdatePostRequest;
import com.fernando.connected_minds_api.requests.params.PaginationQueryParams;
import com.fernando.connected_minds_api.responses.CommentResponse;
import com.fernando.connected_minds_api.responses.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final CommunityRepository communityRepository;
    private final LikePostService likePostService;

    public PostResponse createPost(PostRequest postRequest, User owner) {
        UUID locationID = UUID.fromString(postRequest.locationID());

        if (!communityRepository.existsById(locationID)) {
            throw new EntityNotFoundException("Community or group is not exists");
        }
        Post post = new Post(
                postRequest.content(),
                postRequest.photoURL(),
                locationID,
                owner
        );
        postRepository.save(post);

        return PostResponse.toResponse(post);
    }

    public List<PostResponse> findAllPosts(String locationID) {
        UUID locationIdUUID = UUID.fromString(locationID);

        if (!communityRepository.existsById(locationIdUUID)) {
            throw new EntityNotFoundException("Community or group is not exists");
        }

        return postRepository.findAllByLocationID(locationIdUUID)
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

        Comment comment = new Comment(commentRequest.content(), user, post);

        commentRepository.save(comment);

        return CommentResponse.toResponse(comment);
    }

    public void deletePost(UUID postID, UUID userID) {
        Post post = postRepository.findById(userID)
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
        Pageable pageable = PageRequest.of(pagination.getPage(), pagination.getItemsPerPage(), Sort.by("likes").descending());

        return commentRepository.findAllByPostId(postID, pageable)
                .stream()
                .map(CommentResponse::toResponse)
                .toList();
    }

    public LikePost createLike(User user, UUID postID) {
        Post post = postRepository.findById(postID)
                .orElseThrow(() -> new EntityNotFoundException("Post is not exists"));

        LikePost like = new LikePost(user, post);

        if (post.getOwner().getId() == user.getId()) {
            throw new CannotLikeTheOwnPostException("Actually auth user dont like the own post");
        }

        likePostService.saveLike(like);

        return like;
    }
}