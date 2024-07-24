package com.fernando.connected_minds_api.services;

import com.fernando.connected_minds_api.exceptions.EntityNotFoundException;
import com.fernando.connected_minds_api.models.Comment;
import com.fernando.connected_minds_api.models.Post;
import com.fernando.connected_minds_api.models.User;
import com.fernando.connected_minds_api.repositories.CommentRepository;
import com.fernando.connected_minds_api.repositories.PostRepository;
import com.fernando.connected_minds_api.requests.CommentRequest;
import com.fernando.connected_minds_api.requests.PostRequest;
import com.fernando.connected_minds_api.requests.UpdatePostRequest;
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

    public PostResponse createPost(PostRequest postRequest, User owner) {
        Post post = new Post(
                postRequest.content(),
                postRequest.photoURL(),
                owner
        );
        postRepository.save(post);

        return PostResponse.toResponse(post);
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

    public void deletePost(UUID postID) {
        if (!postRepository.existsById(postID)) {
            throw new EntityNotFoundException("Post is not exists");
        }
        postRepository.deleteById(postID);
    }

    public PostResponse updatePost(UUID postID, UpdatePostRequest postRequest) {
        Post post = postRepository.findById(postID)
                .orElseThrow(() -> new EntityNotFoundException("Post is not exists"));

        if (postRequest.content() != null) {
            post.setContent(postRequest.content());
        }
        if (postRequest.photoURL() != null) {
            post.setPhotoURL(postRequest.photoURL());
        }
        postRepository.save(post);
        return PostResponse.toResponse(post);
    }

    public List<CommentResponse> findAllComments(UUID postID, Integer page, Integer itemsPerPage) {
        if (!postRepository.existsById(postID)) {
            throw new EntityNotFoundException("Post is not exists");
        }
        Pageable pageable = PageRequest.of(page, itemsPerPage, Sort.by("likes").descending());

        return commentRepository.findAllByPostId(postID, pageable)
                .stream()
                .map(CommentResponse::toResponse)
                .toList();
    }
}