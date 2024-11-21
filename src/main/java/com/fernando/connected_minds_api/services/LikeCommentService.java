package com.fernando.connected_minds_api.services;

import java.util.UUID;
import org.springframework.stereotype.Service;
import com.fernando.connected_minds_api.exceptions.EntityNotFoundException;
import com.fernando.connected_minds_api.exceptions.UserIsNotOwnerOfResourceException;
import com.fernando.connected_minds_api.models.Comment;
import com.fernando.connected_minds_api.models.LikeComment;
import com.fernando.connected_minds_api.models.User;
import com.fernando.connected_minds_api.repositories.LikeCommentRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeCommentService {
    private final LikeCommentRepository likeCommentRepository;

    public LikeComment saveLikeComment(User user, Comment comment) {
        LikeComment like = new LikeComment(user, comment);

        likeCommentRepository.save(like);

        return like;
    }

    public void deleteLike(UUID userID, UUID likeID) {
        LikeComment like = likeCommentRepository.findById(likeID)
        .orElseThrow(() -> new EntityNotFoundException("Post like is not exists"));

        if (like.getOwner().getId() != userID) {
            throw new UserIsNotOwnerOfResourceException("%s is not owner of like#%s".
                formatted(like.getOwner().getUsername(), likeID.toString()));
        }
        
        likeCommentRepository.deleteById(likeID);
    }
}