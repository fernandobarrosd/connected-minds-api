package com.fernando.connected_minds_api.services;

import com.fernando.connected_minds_api.enums.NotificationType;
import com.fernando.connected_minds_api.exceptions.EntityNotFoundException;
import com.fernando.connected_minds_api.exceptions.UserIsNotOwnerOfResourceException;
import com.fernando.connected_minds_api.models.Comment;
import com.fernando.connected_minds_api.models.LikeComment;
import com.fernando.connected_minds_api.models.Post;
import com.fernando.connected_minds_api.models.User;
import com.fernando.connected_minds_api.repositories.CommentRepository;
import com.fernando.connected_minds_api.requests.CommentRequest;
import com.fernando.connected_minds_api.requests.NotificationRequest;
import com.fernando.connected_minds_api.requests.UpdateCommentRequest;
import com.fernando.connected_minds_api.requests.params.PaginationQueryParams;
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
    private final LikeCommentService likeCommentService;
    private final NotificationService notificationService;

    public CommentResponse saveComment(String content, User user, Post post) {
        Comment comment = new Comment(content, user, post);

        commentRepository.save(comment);
        User postOwner = post.getOwner();

        String notificationContent = "O %s comentou no post do %s".formatted(user.getUsername(), postOwner.getUsername());

        var notificationRequest = new NotificationRequest(
            comment.getId(),
            notificationContent,
            null,
            NotificationType.COMMENT
        );
        notificationService.saveNotification(notificationRequest, user);

        return CommentResponse.toResponse(comment);
    }

    public List<CommentResponse> findAllComments(PaginationQueryParams pagination, UUID postID) {
        Pageable pageable = PageRequest.of(pagination.getPage(), pagination.getItemsPerPage(), Sort.by("likes").descending());

        return commentRepository.findAllByPostId(postID, pageable)
                .stream()
                .map(CommentResponse::toResponse)
                .toList();
    }

    public CommentResponse findCommentById(UUID commentID) {
        Comment comment = commentRepository.findById(commentID)
                .orElseThrow(() -> new EntityNotFoundException("Comment is not exists"));

        return CommentResponse.toResponse(comment);
    }

    public void deleteComment(UUID commentID, UUID userID) {
        Comment comment = commentRepository.findById(commentID)
                .orElseThrow(() -> new EntityNotFoundException("Comment is not exists"));
        
        if (comment.getOwner().getId() != userID) {
            throw new UserIsNotOwnerOfResourceException("%s is not owner of comment#%s".
                formatted(comment.getOwner().getUsername(), commentID.toString()));
        }
        comment.setComment(null);
        commentRepository.delete(comment);
    }

    public List<CommentResponse> findAllCommentsOfComment(UUID commentID, PaginationQueryParams pagination) {
        if (!commentRepository.existsById(commentID)) {
            throw new EntityNotFoundException("Comment is not exists");
        }
        Pageable pageable = PageRequest.of(pagination.getPage(), pagination.getItemsPerPage(), Sort.by("likes").descending());
        return commentRepository.findAllCommentsOfComment(commentID, pageable)
                .stream()
                .map(CommentResponse::toResponse)
                .toList();
    }
    public CommentResponse saveCommentOfComment(UUID commentID, CommentRequest commentRequest, User owner) {
        Comment comment = commentRepository.findById(commentID)
                .orElseThrow(() -> new EntityNotFoundException("Comment is not exists"));

        Comment commentOfComment = new Comment(commentRequest.content(), comment, owner, comment.getPost());
        commentRepository.save(commentOfComment);

        User commentOwner = comment.getOwner();

        String content = "O %s comentou o comentario %s do %s".formatted(
            owner.getUsername(),
            comment.getContent(),
            commentOwner.getUsername()
        );

        var notificationRequest = new NotificationRequest(
            commentOfComment.getId(),
            content,
            null,
            NotificationType.COMMENT
        );

        notificationService.saveNotification(notificationRequest, owner);
        
        return CommentResponse.toResponse(commentOfComment);
    }

    public CommentResponse updateComment(UUID commentID, UpdateCommentRequest commentRequest, UUID ownerID) {
        Comment comment = commentRepository.findById(commentID)
                .orElseThrow(() -> new EntityNotFoundException("Comment is not exists"));
        
        if (comment.getOwner().getId() != ownerID) {
            throw new UserIsNotOwnerOfResourceException("%s is not owner of comment#%s".
                formatted(comment.getOwner().getUsername(), commentID.toString()));
        }

        if (commentRequest.content() != null) {
            comment.setContent(commentRequest.content());
        }

        commentRepository.save(comment);

        return CommentResponse.toResponse(comment);
    }


    public LikeComment saveLikeComment(User user, UUID commentID) {
        Comment comment = commentRepository.findById(commentID)
                .orElseThrow(() -> new EntityNotFoundException("Comment is not exists"));

        LikeComment like = likeCommentService.saveLikeComment(user, comment);

        return like;
        
    }
}