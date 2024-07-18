package com.fernando.connected_minds_api.repositories;

import com.fernando.connected_minds_api.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
}