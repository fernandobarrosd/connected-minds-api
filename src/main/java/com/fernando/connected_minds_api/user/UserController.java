package com.fernando.connected_minds_api.user;

import com.fernando.connected_minds_api.validation.annotations.UsernamePattern;
import com.fernando.connected_minds_api.validation.annotations.UsernameSize;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping(value = "/{username}", produces = "application/json")
    public ResponseEntity<UserResponse> findUserByUsername(
            @PathVariable
            @Valid
            @UsernameSize(message = "username parameter should be at min 4 character")
            @UsernamePattern(message = "username parameter must have lower case and uppercase letters, numbers and include this characters: [_]")
            String username) {
        return ResponseEntity.ok(userService.findUserByUsername(username));
    }
}