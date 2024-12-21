package com.fernando.connected_minds_api.docs;

import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import com.fernando.connected_minds_api.responses.CommentResponse;
import com.fernando.connected_minds_api.responses.error.ErrorResponseWithFields;
import com.fernando.connected_minds_api.responses.pagination.PaginationResponse;
import com.fernando.connected_minds_api.responses.error.ErrorResponse;
import com.fernando.connected_minds_api.requests.CommentRequest;
import com.fernando.connected_minds_api.requests.UpdateCommentRequest;
import com.fernando.connected_minds_api.docs.responses.CommentPaginationResponse;
import com.fernando.connected_minds_api.models.LikeComment;
import com.fernando.connected_minds_api.models.User;
import com.fernando.connected_minds_api.queryparams.PaginationQueryParams;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Comment", description = "Comment actions")
public interface CommentControllerDocumentation {
    @Operation(
        summary = "Find comment by ID",
        method = "GET",
        operationId = "findCommentById"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Comment is finded with success",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CommentResponse.class)
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
        @ApiResponse(
            responseCode = "404",
            description = "Comment is not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    ResponseEntity<CommentResponse> findCommentById(UUID commentID);


    @Operation(
        summary = "Update comment",
        method = "PATCH",
        operationId = "updateComment"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Comment is updated with success",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CommentResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Request body fields are not valid",
            content = @Content(
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
        @ApiResponse(
            responseCode = "403",
            description = "Authenticated user is not owner of comment",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Comment is not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    ResponseEntity<CommentResponse> updateComment(
        User owner,
        UUID commentID,
        UpdateCommentRequest commentRequest
    );


    @Operation(
        summary = "Delete comment by ID",
        method = "DELETE",
        operationId = "deleteComment"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "Comment is deleted with success"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "This resource require Authentication header JWT token",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Authenticated user is not owner of comment",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Comment is not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
        
    })
    void deleteComment(
        User owner,
        UUID commentID
    );


    @Operation(
        summary = "Create of comment",
        method = "POST",
        operationId = "createCommentOfComment"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "Comment of comment is created with success",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CommentResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Request body fields are not valid",
            content = @Content(
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
        @ApiResponse(
            responseCode = "404",
            description = "Parent comment is not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    ResponseEntity<CommentResponse> createCommentOfComment(
        User owner,
        UUID commentID,
        CommentRequest commentRequest
    );



    @Operation(
        summary = "Find all comments of comment",
        method = "GET",
        operationId = "findAllCommentsOfComment"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CommentPaginationResponse.class)
            )
            
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Query params are not valid",
            content = @Content(
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
        @ApiResponse(
            responseCode = "404",
            description = "Parent comment is not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    ResponseEntity<PaginationResponse<CommentResponse>> findAllCommentsOfComment(
        UUID commentID,
        @ParameterObject PaginationQueryParams queryParams
    );


    @Operation(
        summary = "Create like comment",
        method = "POST",
        operationId = "createLikeComment"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Like comment is created",
            content = @Content(
                schema = @Schema(implementation = LikeComment.class)
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
        @ApiResponse(
            responseCode = "404",
            description = "Comment is not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    ResponseEntity<LikeComment> createLike(
        User user,
        UUID commentID
    );
}