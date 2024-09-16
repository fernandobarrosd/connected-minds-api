package com.fernando.connected_minds_api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PostLocationType {
    COMMUNITY("Community"),
    GROUP("Group");
    
    private final String value;
}