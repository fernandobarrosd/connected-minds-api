package com.fernando.connected_minds_api.docs.responses;

import java.util.List;
import com.fernando.connected_minds_api.responses.CommentResponse;
import com.fernando.connected_minds_api.responses.pagination.PaginationMetadata;
import com.fernando.connected_minds_api.responses.pagination.PaginationResponse;

public class CommentPaginationResponse extends PaginationResponse<CommentResponse>{
    public CommentPaginationResponse(PaginationMetadata metadata, List<CommentResponse> data) {
        super(metadata, data);
    } 
}