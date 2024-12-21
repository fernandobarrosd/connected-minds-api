package com.fernando.connected_minds_api.responses.pagination;


public record PaginationMetadata(
    boolean hasNextPage,
    Integer pagesCount,
    Long elementsCount,
    Integer itemsPerPage,
    Integer page) {}