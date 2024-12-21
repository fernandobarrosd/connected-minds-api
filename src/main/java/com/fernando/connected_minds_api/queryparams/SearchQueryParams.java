package com.fernando.connected_minds_api.queryparams;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchQueryParams extends PaginationQueryParams {
    private String type = "users";

    @NotNull(message = "search query param is required")
    @NotEmpty(message = "search query param should be not empty")
    private String search;
}