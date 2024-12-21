package com.fernando.connected_minds_api.docs;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import com.fernando.connected_minds_api.models.User;
import com.fernando.connected_minds_api.requests.CommunityRequest;
import com.fernando.connected_minds_api.requests.GroupRequest;
import com.fernando.connected_minds_api.responses.CommunityResponse;
import com.fernando.connected_minds_api.responses.CreatePostResponse;
import com.fernando.connected_minds_api.responses.GroupResponse;
import com.fernando.connected_minds_api.responses.error.ErrorResponse;
import com.fernando.connected_minds_api.responses.error.ErrorResponseWithFields;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Community", description = "Community actions")
public interface CommunityControllerDocumentation {

    @Operation(
        summary = "Create community",
        method = "POST",
        operationId = "createCommunity"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Community is created",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CreatePostResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Request body fields are not valid",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseWithFields.class)
            ) 
        ),
        @ApiResponse(
            responseCode = "401",
            description = "This resource require Authentication header JWT token",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
    })
    ResponseEntity<CommunityResponse> createCommunity(
        User user,
        CommunityRequest communityRequest
    );



    @Operation(
        summary = "Create group",
        method = "POST",
        operationId = "createGroup"
    )
    ResponseEntity<GroupResponse> createGroup(
        User user,
        GroupRequest groupRequest,
        UUID communityID
    );
}