package com.fernando.connected_minds_api.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping(value = "/{username}", produces = "application/json")
    public ResponseEntity<UserResponse> findUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.findUserByUsername(username));
    }
}