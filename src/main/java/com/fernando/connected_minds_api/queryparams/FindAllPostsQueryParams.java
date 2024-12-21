package com.fernando.connected_minds_api.queryparams;

import java.util.UUID;
import com.fernando.connected_minds_api.annotations.EnumValidator;
import com.fernando.connected_minds_api.enums.PostLocationType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class FindAllPostsQueryParams extends PaginationQueryParams {
    @NotNull(message = "locationID field is required")
    UUID locationID;

    @NotNull(message = "content field is required")
    @EnumValidator(enumValues = {"COMMUNITY", "GROUP"},  message = "locationType field should be COMMUNITY or GROUP")
    PostLocationType locationType;
    
}