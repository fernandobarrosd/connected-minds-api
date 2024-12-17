package com.fernando.connected_minds_api.responses;

import java.util.List;
import org.springframework.data.domain.Page;

public record PaginationResponse<T>(Metadata metadata, List<T> data) {
   static public record Metadata(
    boolean hasNextPage,
    Integer pagesCount,
    Long elementsCount,
    Integer itemsPerPage,
    Integer page) {}

    public static <T> PaginationResponse<T> toResponse(Page<?> page, List<T> data, Integer itemsPerPage, Integer currentPage) {
        Metadata metadata = new Metadata(
            page.hasNext(),
            page.getTotalPages(),
            page.getTotalElements(),
            itemsPerPage,
            currentPage
        );

        return new PaginationResponse<T>(metadata, data);
    }
}