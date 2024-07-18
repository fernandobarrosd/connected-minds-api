package com.fernando.connected_minds_api.services;

import com.fernando.connected_minds_api.exceptions.EntityNotFoundException;
import com.fernando.connected_minds_api.models.Comment;
import com.fernando.connected_minds_api.models.Post;
import com.fernando.connected_minds_api.models.User;
import com.fernando.connected_minds_api.repositories.CommentRepository;
import com.fernando.connected_minds_api.repositories.PostRepository;
import com.fernando.connected_minds_api.requests.CommentRequest;
import com.fernando.connected_minds_api.requests.PostRequest;
import com.fernando.connected_minds_api.responses.CommentResponse;
import com.fernando.connected_minds_api.responses.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public PostResponse createPost(PostRequest postRequest, User owner) {
        Post post = new Post(
                postRequest.content(),
                postRequest.photoURL(),
                owner
        );
        postRepository.save(post);

        var ownerResponse = PostResponse.OwnerResponse.builder()
                .id(owner.getId().toString())
                .username(owner.getUsername())
                .photoURL(owner.getPhotoURL())
                .build();

        return PostResponse.builder()
                .id(post.getId().toString())
                .likes(post.getLikes())
                .content(post.getContent())
                .createdAt(post.getCreatedAt().toString())
                .photoURL(post.getPhotoURL())
                .owner(ownerResponse)
                .build();
    }

    public PostResponse findPostByID(UUID postID) {
        Optional<Post> postOptional = postRepository.findById(postID);
        if (postOptional.isEmpty()) {
            throw new EntityNotFoundException("Post is not exists");
        }
        Post post = postOptional.get();
        return PostResponse.fromEntity(post);
    }

    public CommentResponse createComment(User user, UUID postID, CommentRequest commentRequest) {
        Post post = postRepository.findById(postID)
                .orElseThrow(() -> new EntityNotFoundException("Post is not exists"));

        Comment comment = new Comment(commentRequest.content(), user, post);

        commentRepository.save(comment);

        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .likes(comment.getLikes())
                .createdAt(comment.getCreatedAt().toString())
                .ownerID(user.getId())
                .postID(post.getId())
                .build();
    }
}