package com.fernando.connected_minds_api.services;

import org.springframework.stereotype.Service;
import com.fernando.connected_minds_api.queryparams.SearchQueryParams;
import com.fernando.connected_minds_api.responses.PaginationResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final UserService userService;
    private final CommunityService communityService;


    public PaginationResponse<?> search(SearchQueryParams queryParams) {
        String type = queryParams.getType();

        if (type.contentEquals("users")) {
            return userService.searchUser(
                queryParams.getSearch(), 
                queryParams.getPage(), 
                queryParams.getItemsPerPage()
            );
        }
        else {
            return communityService.searchCommunity(
                queryParams.getSearch(),
                queryParams.getPage(),
                queryParams.getItemsPerPage()
            );
        }
    }
}