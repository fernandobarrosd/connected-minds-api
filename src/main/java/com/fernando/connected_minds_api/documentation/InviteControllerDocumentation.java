package com.fernando.connected_minds_api.documentation;

import java.util.UUID;
import org.springframework.http.ResponseEntity;
import com.fernando.connected_minds_api.models.User;
import com.fernando.connected_minds_api.requests.InviteRequest;
import com.fernando.connected_minds_api.responses.InviteResponse;
import com.fernando.connected_minds_api.responses.error.ErrorResponse;
import com.fernando.connected_minds_api.responses.error.ErrorResponseWithFields;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Invite", description = "Invite actions")
public interface InviteControllerDocumentation {
    @Operation(
        summary = "Create invite",
        method = "POST",
        operationId = "createInvite",
        requestBody = @RequestBody(
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = InviteRequest.class)
            )
        )
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Invite is created",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = InviteResponse.class)
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
            responseCode = "404",
            description = "Not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
    })
    ResponseEntity<InviteResponse> createInvite(InviteRequest inviteRequest);


    @Operation(
        summary = "Accept invite",
        method = "GET",
        operationId = "acceptInvite"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "User accept the invite",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = InviteResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "User is already exists in community",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )

        ),
        @ApiResponse(
            responseCode = "404",
            description = "Not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
    })
    ResponseEntity<InviteResponse> acceptInvite(User user, UUID inviteID);
}
