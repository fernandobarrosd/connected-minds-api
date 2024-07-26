package com.fernando.connected_minds_api.repositories;

import com.fernando.connected_minds_api.models.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface CommunityRepository extends JpaRepository<Community, UUID> {
}