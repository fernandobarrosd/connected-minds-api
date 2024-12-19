package com.fernando.connected_minds_api.repositories;

import com.fernando.connected_minds_api.models.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;


public interface PostRepository extends JpaRepository<Post, UUID> {
    Page<Post> findAllByLocationID(UUID locationID, Pageable pageable);
}