package com.fernando.connected_minds_api.repositories;

import com.fernando.connected_minds_api.models.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;


public interface CommunityRepository extends JpaRepository<Community, UUID> {
    Page<Community> findAllByNameContaining(String name, Pageable pageable);
}