package com.fernando.connected_minds_api.services;

import java.util.UUID;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import com.fernando.connected_minds_api.models.Notification;
import com.fernando.connected_minds_api.models.User;
import com.fernando.connected_minds_api.repositories.NotificationRepository;
import com.fernando.connected_minds_api.requests.NotificationRequest;
import com.fernando.connected_minds_api.responses.NotificationResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public NotificationResponse saveNotification(NotificationRequest notificationRequest, User owner) {
        Notification notification = notificationRequest.toEntity();
        notification.setOwner(owner);

        notificationRepository.save(notification);

        return NotificationResponse.toResponse(notification);
    }


    public void sendNotification(UUID userID, NotificationResponse notificationResponse) {
        simpMessagingTemplate.convertAndSendToUser(userID.toString(), "/topic/notifications", notificationResponse);
    }
}