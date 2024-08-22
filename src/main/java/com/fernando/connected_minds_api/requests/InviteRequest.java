package com.fernando.connected_minds_api.requests;

import java.util.UUID;

public record InviteRequest(
    UUID fromID) {}