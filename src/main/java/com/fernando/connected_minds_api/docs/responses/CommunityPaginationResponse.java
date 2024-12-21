package com.fernando.connected_minds_api.docs.responses;

import com.fernando.connected_minds_api.responses.pagination.PaginationResponse;
import com.fernando.connected_minds_api.responses.pagination.PaginationMetadata;
import com.fernando.connected_minds_api.responses.CommunityResponse;
import java.util.List;

public class CommunityPaginationResponse extends PaginationResponse<CommunityResponse>{
    public CommunityPaginationResponse(PaginationMetadata metadata, List<CommunityResponse> data) {
        super(metadata, data);
    }
}