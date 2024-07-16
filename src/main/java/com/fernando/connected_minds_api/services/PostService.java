package com.fernando.connected_minds_api.services;

import com.fernando.connected_minds_api.models.Post;
import com.fernando.connected_minds_api.models.User;
import com.fernando.connected_minds_api.repositories.PostRepository;
import com.fernando.connected_minds_api.requests.PostRequest;
import com.fernando.connected_minds_api.responses.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public PostResponse createPost(PostRequest postRequest, User owner) {
        Post post = new Post(
                postRequest.content(),
                postRequest.photoURL(),
                owner
        );
        postRepository.save(post);

        PostResponse.Owner ownerResponse = PostResponse.Owner.builder()
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
}