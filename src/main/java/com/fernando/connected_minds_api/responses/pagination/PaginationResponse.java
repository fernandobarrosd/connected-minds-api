package com.fernando.connected_minds_api.responses.pagination;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PaginationResponse<T> {
    private PaginationMetadata metadata;
    private List<T> data;

    public static <T> PaginationResponse<T> toResponse(
        boolean hasNextPage,
        Integer pagesCount,
        Long elementsCount,
        Integer itemsPerPage,
        Integer page,
        List<T> data) {

        PaginationMetadata metadata = new PaginationMetadata(
            hasNextPage,
            pagesCount,
            elementsCount,
            itemsPerPage,
            page
        );

        return new PaginationResponse<T>(metadata, data);
    }
}