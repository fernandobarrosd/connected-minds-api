package com.fernando.connected_minds_api.models;

import java.util.UUID;
import com.fernando.connected_minds_api.models.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "invite_table")
@NoArgsConstructor
@Getter
@Setter
public class Invite extends BaseEntity {
    private UUID fromID;

    public Invite(UUID fromID) {
        super(null);
        this.fromID = fromID;
    }
}