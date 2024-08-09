package com.fernando.connected_minds_api.repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.domain.Pageable;
import com.fernando.connected_minds_api.models.Group;

public interface GroupRepository extends JpaRepository<Group, UUID>{
    List<Group> findAllByCommunityId(UUID communityId, Pageable pageable);
}