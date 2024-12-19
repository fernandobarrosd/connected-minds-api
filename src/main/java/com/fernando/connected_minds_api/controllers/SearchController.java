package com.fernando.connected_minds_api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fernando.connected_minds_api.queryparams.SearchQueryParams;
import com.fernando.connected_minds_api.responses.PaginationResponse;
import com.fernando.connected_minds_api.services.SearchService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<PaginationResponse<?>> search(SearchQueryParams searchQueryParam) {
        return ResponseEntity.ok(searchService.search(searchQueryParam));
    }
}