package com.fernando.connected_minds_api.user;

import com.fernando.connected_minds_api.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User findUserByID(UUID userID) {
        return userRepository.findById(userID)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public UserResponse findUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return user.toResponse();
    }

    public boolean existsUserByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        return userOptional.isPresent();
    }

    public boolean existsUserByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);

        return userOptional.isPresent();
    }
}
