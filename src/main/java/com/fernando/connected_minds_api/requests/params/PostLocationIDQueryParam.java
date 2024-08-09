package com.fernando.connected_minds_api.requests.params;

import jakarta.validation.constraints.NotNull;

public record PostLocationIDQueryParam(
    @NotNull(message = "locationID query param is required")
    String locationID) {}