package com.fernando.connected_minds_api.requests;

import java.util.UUID;

import com.fernando.connected_minds_api.enums.InviteType;

public record InviteRequest(
    UUID fromID,
    InviteType type) {}