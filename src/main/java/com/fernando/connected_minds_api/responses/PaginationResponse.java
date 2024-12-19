package com.fernando.connected_minds_api.responses;

import java.util.List;

public record PaginationResponse<T>(Metadata metadata, List<T> data) {
   static public record Metadata(
    boolean hasNextPage,
    Integer pagesCount,
    Long elementsCount,
    Integer itemsPerPage,
    Integer page) {}

    public static <T> PaginationResponse<T> toResponse(
        boolean hasNextPage,
        Integer pagesCount,
        Long elementsCount,
        Integer itemsPerPage,
        Integer page,
        List<T> data) {
        Metadata metadata = new Metadata(
            hasNextPage,
            pagesCount,
            elementsCount,
            itemsPerPage,
            page
        );

        return new PaginationResponse<T>(metadata, data);
    }
}