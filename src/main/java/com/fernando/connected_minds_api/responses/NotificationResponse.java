package com.fernando.connected_minds_api.responses;

import java.util.UUID;
import com.fernando.connected_minds_api.enums.NotificationType;
import com.fernando.connected_minds_api.models.Notification;

public record NotificationResponse(
    UUID id,
    UUID fromID,
    String content,
    String photoURL,
    NotificationType type,
    String createdAt) {

        public static NotificationResponse toResponse(Notification notification) {
            return new NotificationResponse(
                notification.getId(),
                notification.getFromID(),
                notification.getContent(),
                notification.getPhotoURL(),
                notification.getType(),
                notification.getCreatedAt().toString()
            );
        } 
    
}
