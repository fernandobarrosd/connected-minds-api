package com.fernando.connected_minds_api.docs;

import java.util.UUID;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;

import com.fernando.connected_minds_api.docs.responses.CommentPaginationResponse;
import com.fernando.connected_minds_api.docs.responses.PostPaginationResponse;
import com.fernando.connected_minds_api.models.LikePost;
import com.fernando.connected_minds_api.models.User;
import com.fernando.connected_minds_api.queryparams.FindAllPostsQueryParams;
import com.fernando.connected_minds_api.queryparams.PaginationQueryParams;
import com.fernando.connected_minds_api.requests.CommentRequest;
import com.fernando.connected_minds_api.requests.PostRequest;
import com.fernando.connected_minds_api.requests.UpdatePostRequest;
import com.fernando.connected_minds_api.responses.CommentResponse;
import com.fernando.connected_minds_api.responses.CreatePostResponse;
import com.fernando.connected_minds_api.responses.PostResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.fernando.connected_minds_api.responses.error.ErrorResponse;
import com.fernando.connected_minds_api.responses.error.ErrorResponseWithFields;
import com.fernando.connected_minds_api.responses.pagination.PaginationResponse;

@Tag(name = "Post", description = "Post actions")
public interface PostControllerDocumentation {
    @Operation(
        summary = "Create post endpoint",
        method = "POST",
        operationId = "createPost",
        requestBody = @RequestBody(
            required = true,
            content = @Content(
                schema = @Schema(implementation = PostRequest.class)
            )
        )
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Post is created",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CreatePostResponse.class)
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
            description = "Community or group is not exists",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Request body fields are not valid",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseWithFields.class)
            ) 
        )
    })
    ResponseEntity<CreatePostResponse> createPost(User user, PostRequest postRequest);


    @Operation(
        summary = "Find all posts",
        method = "GET",
        operationId = "findAllPosts"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Posts are finded",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PostPaginationResponse.class)
                
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
            description = "Not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    ResponseEntity<PaginationResponse<PostResponse>> findAllPosts(
       @ParameterObject FindAllPostsQueryParams queryParams
    );



    @Operation(
        summary = "Find post by ID",
        method = "GET",
        operationId = "findPostByID"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Post is finded with success",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PostResponse.class)
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
            description = "Post is not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    ResponseEntity<PostResponse> findPostByID(UUID postID);


    @Operation(
        summary = "Update post",
        method = "PATCH",
        operationId = "updatePost",
        requestBody = @RequestBody(
            required = true,
            content = @Content(
                schema = @Schema(implementation = UpdatePostRequest.class)
            )
        )
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Post is updated",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PostResponse.class)
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
            description = "Authenticated user is not owner of post",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Post is not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    ResponseEntity<PostResponse> updatePost(User user, UUID postID, UpdatePostRequest request);


    @Operation(
        summary = "Delete post by ID",
        method = "DELETE",
        operationId = "deletePost"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "Post is deleted with success"
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
            description = "Authenticated user is not owner of post",
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
        )
    })
    void deletePost(User user, UUID postID);
    


    @Operation(
        summary = "Create post comment",
        method = "POST",
        operationId = "createComment",
        requestBody = @RequestBody(
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CommentRequest.class)
            )
        )
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Comment is created",
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
            description = "Post is not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    ResponseEntity<CommentResponse> createComment(
        User user,
        UUID postID,
        CommentRequest commentRequest
    );


    @Operation(
        summary = "Find all post comments",
        method = "GET",
        operationId = "findAllComments"

    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Finded all post comments",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CommentPaginationResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Pagination parameters are not valid",
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
            description = "Post is not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    ResponseEntity<PaginationResponse<CommentResponse>> findAllComments(
        UUID postID,
        @ParameterObject PaginationQueryParams pagination
    );


    @Operation(
        summary = "Create post like",
        method = "POST",
        operationId = "createLike"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Post like is created",
            content = @Content(
                schema = @Schema(implementation = LikePost.class)
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
            description = "Post is not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    ResponseEntity<LikePost> createLike(User user, UUID postID);
}