package com.fernando.connected_minds_api.controllers;

import com.fernando.connected_minds_api.documentation.AuthControllerDocumentation;
import com.fernando.connected_minds_api.requests.LoginRequest;
import com.fernando.connected_minds_api.requests.RefreshTokenRequest;
import com.fernando.connected_minds_api.requests.RegisterRequest;
import com.fernando.connected_minds_api.responses.AuthResponse;
import com.fernando.connected_minds_api.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController implements AuthControllerDocumentation {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody @Valid LoginRequest login) {
        return ResponseEntity.ok(authService.authenticate(login));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody @Valid RegisterRequest registerRequest) {
        return ResponseEntity.created(null).body(authService.registerUser(registerRequest));
    }

    @PostMapping("/token")
    public ResponseEntity<AuthResponse> generateNewToken(@RequestBody @Valid RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.generateNewToken(request));
    }
}