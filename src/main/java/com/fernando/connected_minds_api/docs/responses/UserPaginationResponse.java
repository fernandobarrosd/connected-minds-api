package com.fernando.connected_minds_api.docs.responses;

import java.util.List;
import com.fernando.connected_minds_api.responses.UserResponse;
import com.fernando.connected_minds_api.responses.pagination.PaginationResponse;
import com.fernando.connected_minds_api.responses.pagination.PaginationMetadata;


public class UserPaginationResponse extends PaginationResponse<UserResponse> {
    public UserPaginationResponse(PaginationMetadata metadata, List<UserResponse> data) {
        super(metadata, data);
    }
}