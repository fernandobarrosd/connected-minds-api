package com.fernando.connected_minds_api.services;

import com.fernando.connected_minds_api.exceptions.EntityNotFoundException;
import com.fernando.connected_minds_api.models.Community;
import com.fernando.connected_minds_api.models.User;
import com.fernando.connected_minds_api.queryparams.PaginationQueryParams;
import com.fernando.connected_minds_api.repositories.UserRepository;
import com.fernando.connected_minds_api.responses.CommunityResponse;
import com.fernando.connected_minds_api.responses.UserResponse;
import com.fernando.connected_minds_api.responses.pagination.PaginationResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

    public PaginationResponse<CommunityResponse> findAllUserCommunities(UUID userID, PaginationQueryParams queryParams) {
        Integer itemsPerPage = queryParams.getItemsPerPage();
        Integer page = queryParams.getPage();

        Pageable pageable = PageRequest.of(page, itemsPerPage);

        Page<Community> communityPage = userRepository.findAllCommunities(userID, pageable);

        List<CommunityResponse> communities = communityPage
        .stream()
        .map(CommunityResponse::toResponse)
        .toList();

        return PaginationResponse.toResponse(
            communityPage.hasNext(),
            communityPage.getTotalPages(),
            communityPage.getTotalElements(),
            itemsPerPage,
            page,
            communities
        );
    }

    public User findUserById(UUID userID) {
        return userRepository.findById(userID)
        .orElseThrow(() -> new EntityNotFoundException("User is not exists"));
    }

    public PaginationResponse<UserResponse> searchUser(String search, Integer page, Integer itemsPerPage) {
        Pageable pageable = PageRequest.of(page, itemsPerPage);
        Page<User> userPage = userRepository.findAllByUsernameContaining(search, pageable);

        List<UserResponse> users = userPage.get()
        .map(UserResponse::toResponse)
        .toList();

        return PaginationResponse.toResponse(
            userPage.hasNext(),
            userPage.getTotalPages(),
            userPage.getTotalElements(),
            itemsPerPage,
            page,
            users
        );
    }
}