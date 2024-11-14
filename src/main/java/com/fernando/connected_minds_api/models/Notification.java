package com.fernando.connected_minds_api.models;

import java.time.LocalDateTime;
import com.fernando.connected_minds_api.models.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Entity
public class Notification extends BaseEntity {
    @Column(nullable = false)
    private String content;

    @Column(name = "photo_url", nullable = false)
    private String photoURL;

    @Column(name = "is_viewed", nullable = false)
    private Boolean isViewed;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public Notification(String content, String photoURL, Boolean isViewed) {
        super(null);
        this.content = content;
        this.photoURL = photoURL;
        this.isViewed = false;
        this.createdAt = LocalDateTime.now();
    }
}