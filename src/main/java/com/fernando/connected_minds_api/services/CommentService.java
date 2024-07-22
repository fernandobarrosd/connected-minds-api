package com.fernando.connected_minds_api.services;

import com.fernando.connected_minds_api.exceptions.EntityNotFoundException;
import com.fernando.connected_minds_api.models.Comment;
import com.fernando.connected_minds_api.repositories.CommentRepository;
import com.fernando.connected_minds_api.responses.CommentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public void deleteComment(UUID commentID) {
        if (!commentRepository.existsById(commentID)) {
            throw new EntityNotFoundException("Comment is not exists");
        }
        commentRepository.deleteById(commentID);
    }

    public CommentResponse findCommentById(UUID commentID) {
        Comment comment = commentRepository.findById(commentID)
                .orElseThrow(() -> new EntityNotFoundException("Comment is not exists"));

        return CommentResponse.fromEntity(comment);
    }
}