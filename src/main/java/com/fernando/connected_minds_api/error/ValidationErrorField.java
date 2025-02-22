package com.fernando.connected_minds_api.error;

import lombok.Builder;

@Builder
public record ValidationErrorField(String field, String message) {}