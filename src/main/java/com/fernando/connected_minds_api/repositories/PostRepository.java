package com.fernando.connected_minds_api.repositories;

import com.fernando.connected_minds_api.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;


public interface PostRepository extends JpaRepository<Post, UUID> {
    List<Post> findAllByLocationID(UUID locationID);
}