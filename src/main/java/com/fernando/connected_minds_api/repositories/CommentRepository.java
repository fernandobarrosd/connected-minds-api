package com.fernando.connected_minds_api.repositories;

import com.fernando.connected_minds_api.models.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    @Query("SELECT c FROM Comment c WHERE c.post.id = :postID AND c.comment IS null")
    Page<Comment> findAllByPostId(UUID postID, Pageable pageable);

    @Query("SELECT c.comments FROM Comment c WHERE c.id = :commentID")
    Page<Comment> findAllCommentsOfComment(UUID commentID, Pageable pageable);
}