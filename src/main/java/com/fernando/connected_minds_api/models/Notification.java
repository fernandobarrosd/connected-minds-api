package com.fernando.connected_minds_api.models;

import java.time.LocalDateTime;
import java.util.UUID;
import com.fernando.connected_minds_api.enums.NotificationType;
import com.fernando.connected_minds_api.models.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "notification_table")
public class Notification extends BaseEntity {
    @Column(name = "from_id", nullable = false)
    private UUID fromID;

    @Column(nullable = false)
    private String content;

    @Column(name = "photo_url")
    private String photoURL;

    @Column(name = "is_viewed", nullable = false)
    private Boolean isViewed;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private NotificationType type;

    @ManyToOne
    private User owner;

    public Notification(String content, String photoURL, NotificationType type, UUID fromID) {
        super(null);
        this.content = content;
        this.photoURL = photoURL;
        this.isViewed = false;
        this.createdAt = LocalDateTime.now();
        this.type = type;
        this.fromID = fromID;
    }
}