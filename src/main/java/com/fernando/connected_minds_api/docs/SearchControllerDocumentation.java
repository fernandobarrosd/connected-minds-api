package com.fernando.connected_minds_api.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import com.fernando.connected_minds_api.docs.responses.UserPaginationResponse;
import com.fernando.connected_minds_api.queryparams.SearchQueryParams;
import com.fernando.connected_minds_api.responses.error.ErrorResponseWithFields;
import com.fernando.connected_minds_api.responses.pagination.PaginationResponse;
import org.springframework.http.ResponseEntity;


@Tag(name = "Search", description = "Search actions")
public interface SearchControllerDocumentation {
    @Operation(
        summary = "Search endpoint",
        method = "GET",
        operationId = "search"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Searched users",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserPaginationResponse.class)
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
    ResponseEntity<PaginationResponse<?>> search(
        @ParameterObject SearchQueryParams searchQueryParam
    );
}