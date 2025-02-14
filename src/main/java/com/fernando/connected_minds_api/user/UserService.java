package com.fernando.connected_minds_api.user;

import org.springframework.stereotype.Service;

import com.fernando.connected_minds_api.exceptions.EntityNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserResponse findUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new EntityNotFoundException("User is not found"));

        return UserResponse.toResponse(user);
    }
}