package com.fernando.connected_minds_api.documentation;

import org.springframework.http.ResponseEntity;
import com.fernando.connected_minds_api.requests.LoginRequest;
import com.fernando.connected_minds_api.requests.RefreshTokenRequest;
import com.fernando.connected_minds_api.requests.RegisterRequest;
import com.fernando.connected_minds_api.responses.AuthResponse;
import com.fernando.connected_minds_api.responses.error.ErrorResponse;
import com.fernando.connected_minds_api.responses.error.ErrorResponseWithFields;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Auth", description = "User authentication actions")
public interface AuthControllerDocumentation {
    @Operation(
        summary = "Authenticate user endpoint",
        method = "POST",
        operationId = "loginUser"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Authentication is success",
            content = @Content(
                schema = @Schema(implementation = AuthResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Error when authenticate user why user is not exists",
            content = @Content(
                schema = @Schema(implementation = ErrorResponse.class)
            ) 
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Request body fields are not valid",
            content = @Content(
                schema = @Schema(implementation = ErrorResponseWithFields.class)
            ) 
        )
    })
    public ResponseEntity<AuthResponse> authenticate(LoginRequest loginRequest);


    @Operation(
        description = "Register user and authenticate",
        method = "POST",
        operationId = "registerUser"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "User is registered with success",
            content = @Content(
                schema = @Schema(implementation = AuthResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Error when register user why user is already exists",
            content = @Content(
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Request body fields are not valid",
            content = @Content(
                schema = @Schema(implementation = ErrorResponseWithFields.class)
            ) 
        )
    })
    public ResponseEntity<AuthResponse> registerUser(RegisterRequest registerRequest);


    @Operation(
        description = "Generate new JWT token",
        method = "POST",
        operationId = "generateNewToken"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Token is regenerated with success",
            content = @Content(
                schema = @Schema(implementation = AuthResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Error when authenticate user why user is not exists",
            content = @Content(
                schema = @Schema(implementation = ErrorResponse.class)
            ) 
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Refresh token is not valid",
            content = @Content(
                schema = @Schema(implementation = AuthResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Request body fields are not valid",
            content = @Content(
                schema = @Schema(implementation = ErrorResponseWithFields.class)
            ) 
        )
    })
    public ResponseEntity<AuthResponse> generateNewToken(RefreshTokenRequest refreshTokenRequest);
}