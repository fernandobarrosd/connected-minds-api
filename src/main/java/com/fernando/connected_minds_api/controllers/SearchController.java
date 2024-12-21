package com.fernando.connected_minds_api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fernando.connected_minds_api.queryparams.SearchQueryParams;
import com.fernando.connected_minds_api.responses.pagination.PaginationResponse;
import com.fernando.connected_minds_api.services.SearchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.fernando.connected_minds_api.docs.SearchControllerDocumentation;


@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController implements SearchControllerDocumentation {
    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<PaginationResponse<?>> search(@Valid SearchQueryParams searchQueryParam) {
        return ResponseEntity.ok(searchService.search(searchQueryParam));
    }
}