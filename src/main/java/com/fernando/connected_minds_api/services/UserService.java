package com.fernando.connected_minds_api.services;

import com.fernando.connected_minds_api.exceptions.EntityNotFoundException;
import com.fernando.connected_minds_api.models.User;
import com.fernando.connected_minds_api.repositories.UserRepository;
import com.fernando.connected_minds_api.requests.params.PaginationQueryParams;
import com.fernando.connected_minds_api.responses.CommunityResponse;
import com.fernando.connected_minds_api.responses.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserResponse findUserByUsername(String username) {
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("%s is not exists".formatted(username)));

        return UserResponse.toResponse(user);
    }

    public List<CommunityResponse> findAllUserCommunities(UUID userID, PaginationQueryParams pagination) {
        Pageable pageable = PageRequest.of(pagination.getPage(), pagination.getItemsPerPage());
        return userRepository.findAllCommunities(userID, pageable)
                .stream()
                .map(CommunityResponse::toResponse)
                .toList();
    }

    public User findUserById(UUID userID) {
        return userRepository.findById(userID)
        .orElseThrow(() -> new EntityNotFoundException("User is not exists"));
    }
}