package com.fernando.connected_minds_api.requests.params;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginationQueryParams {
    @PositiveOrZero(message = "page should be positive number")
    private Integer page = 0;

    @PositiveOrZero(message = "page should be positive number")
    private  Integer itemsPerPage = 5;
}