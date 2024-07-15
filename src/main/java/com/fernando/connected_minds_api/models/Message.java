package com.fernando.connected_minds_api.models;

import com.fernando.connected_minds_api.models.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;


@Entity
@Table(name = "message_table")
@NoArgsConstructor
@Getter
@Setter
public class Message extends BaseEntity {
    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(name = "send_at", nullable = false)
    private LocalDateTime sendAt;

    public Message(String content, User owner) {
        super(null);
        this.content = content;
        this.owner = owner;
        this.sendAt = LocalDateTime.now();
    }
}
