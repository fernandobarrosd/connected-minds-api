package com.fernando.connected_minds_api.requests;

import java.util.UUID;

import com.fernando.connected_minds_api.enums.NotificationType;
import com.fernando.connected_minds_api.models.Notification;

public record NotificationRequest(
    UUID fromID,
    String content,
    String photoURL,
    NotificationType type) {

    public Notification toEntity() {
        return new Notification(
            content,
            photoURL,
            type,
            fromID
        );
    }

        
    }