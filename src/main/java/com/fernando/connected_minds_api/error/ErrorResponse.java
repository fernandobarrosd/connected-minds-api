package com.fernando.connected_minds_api.error;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Builder
@Getter
public class ErrorResponse {
    private final String message;
    private final String path;
    private final Integer statusCode;
    private final LocalDateTime date;
}
