package com.fernando.connected_minds_api.services;

import java.util.UUID;
import org.springframework.stereotype.Service;
import com.fernando.connected_minds_api.exceptions.EntityNotFoundException;
import com.fernando.connected_minds_api.exceptions.UserIsNotOwnerOfResourceException;
import com.fernando.connected_minds_api.models.LikePost;
import com.fernando.connected_minds_api.repositories.LikePostRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikePostService {
    private final LikePostRepository likePostRepository; 
    
    public LikePost saveLike(LikePost like) {
        return likePostRepository.save(like);
    }

    public void deleteLike(UUID userID, UUID likeID) {
        LikePost like = likePostRepository.findById(likeID)
        .orElseThrow(() -> new EntityNotFoundException("Post like is not exists"));

        if (like.getOwner().getId() != userID) {
            throw new UserIsNotOwnerOfResourceException("%s is not owner of like#%s".
                formatted(like.getOwner().getUsername(), likeID.toString()));
        }
        
        likePostRepository.deleteById(likeID);
    }
}