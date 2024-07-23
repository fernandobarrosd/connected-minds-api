package com.fernando.connected_minds_api.services;

import com.fernando.connected_minds_api.exceptions.EntityNotFoundException;
import com.fernando.connected_minds_api.models.Comment;
import com.fernando.connected_minds_api.models.User;
import com.fernando.connected_minds_api.repositories.CommentRepository;
import com.fernando.connected_minds_api.requests.CommentRequest;
import com.fernando.connected_minds_api.responses.CommentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
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

        return CommentResponse.toResponse(comment);
    }

    public List<CommentResponse> findAllCommentsOfComment(UUID commentID, Integer page, Integer itemsPerPage) {
        if (!commentRepository.existsById(commentID)) {
            throw new EntityNotFoundException("Comment is not exists");
        }
        Pageable pageable = PageRequest.of(page, itemsPerPage, Sort.by("likes").descending());
        return commentRepository.findAllCommentsOfComment(commentID, pageable)
                .stream()
                .map(CommentResponse::toResponse)
                .toList();
    }

    public CommentResponse createCommentOfComment(UUID commentID, CommentRequest commentRequest, User owner) {
        Comment comment = commentRepository.findById(commentID)
                .orElseThrow(() -> new EntityNotFoundException("Comment is not exists"));

        Comment commentOfComment = new Comment(commentRequest.content(), comment, owner, comment.getPost());
        commentRepository.save(commentOfComment);

        return CommentResponse.toResponse(commentOfComment);
    }
}