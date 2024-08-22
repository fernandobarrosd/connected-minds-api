package com.fernando.connected_minds_api.repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.fernando.connected_minds_api.models.Invite;

public interface InviteRepository extends JpaRepository<Invite, UUID>{
    
}