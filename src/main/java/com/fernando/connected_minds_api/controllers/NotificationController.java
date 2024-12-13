package com.fernando.connected_minds_api.controllers;

import java.util.UUID;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import com.fernando.connected_minds_api.models.User;
import com.fernando.connected_minds_api.payloads.NotificationPayload;
import com.fernando.connected_minds_api.services.NotificationService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @MessageMapping("notification/{notificationID}/send")
    public void sendNotification(
        @DestinationVariable UUID notificationID, 
        @Payload NotificationPayload payload,
        StompHeaderAccessor accessor) {
            var usernamePasswordToken = (UsernamePasswordAuthenticationToken) accessor.getUser();
            User user = (User) usernamePasswordToken.getPrincipal();
            notificationService.sendNotification(user, notificationID, payload);
    }
    
}