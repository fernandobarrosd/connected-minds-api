package com.fernando.connected_minds_api.responses.error;

import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record ErrorResponse(
        String message,
        String path,
        Integer statusCode,
        LocalDateTime date) {}