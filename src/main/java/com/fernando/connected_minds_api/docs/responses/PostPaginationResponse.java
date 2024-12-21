package com.fernando.connected_minds_api.docs.responses;

import java.util.List;

import com.fernando.connected_minds_api.responses.PostResponse;
import com.fernando.connected_minds_api.responses.pagination.PaginationMetadata;
import com.fernando.connected_minds_api.responses.pagination.PaginationResponse;

public class PostPaginationResponse extends PaginationResponse<PostResponse>{
    public PostPaginationResponse(PaginationMetadata metadata, List<PostResponse> data) {
        super(metadata, data);
    } 
}