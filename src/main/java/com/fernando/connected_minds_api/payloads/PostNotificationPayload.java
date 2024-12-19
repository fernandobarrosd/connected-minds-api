package com.fernando.connected_minds_api.payloads;

import java.util.List;
import com.fernando.connected_minds_api.enums.NotificationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class PostNotificationPayload extends NotificationPayload {
    private List<String> locationMembersUsernames;
    
    public PostNotificationPayload(NotificationType type, List<String> locationMembersUsernames) {
        super(type);
        this.locationMembersUsernames = locationMembersUsernames;
    }
    
}