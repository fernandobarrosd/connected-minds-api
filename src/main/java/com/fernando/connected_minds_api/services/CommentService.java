package com.fernando.connected_minds_api.services;

import com.fernando.connected_minds_api.exceptions.EntityNotFoundException;
import com.fernando.connected_minds_api.repositories.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public void deleteComment(UUID commentID) {
        if (!commentRepository.existsById(commentID)) {
            throw new EntityNotFoundException("Post is not exists");
        }
        commentRepository.deleteById(commentID);
    }
}