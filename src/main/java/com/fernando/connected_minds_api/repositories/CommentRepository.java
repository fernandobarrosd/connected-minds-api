package com.fernando.connected_minds_api.repositories;

import com.fernando.connected_minds_api.models.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    List<Comment> findAllByPostId(UUID postId, Pageable pageable);

    @Query("SELECT c.comments FROM Comment c WHERE c.id = :commentID")
    List<Comment> findAllCommentsOfComment(UUID commentID, Pageable pageable);
}