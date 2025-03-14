package com.fernando.connected_minds_api.errors.validation;

import lombok.Builder;

@Builder
public record ValidationErrorField(String field, String message) {}