package com.fernando.connected_minds_api.repositories;

import com.fernando.connected_minds_api.models.Community;
import com.fernando.connected_minds_api.models.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    @Query("SELECT u.communities FROM User u WHERE u.id = :userID")
    List<Community> findAllCommunities(UUID userID, Pageable pageable);
}