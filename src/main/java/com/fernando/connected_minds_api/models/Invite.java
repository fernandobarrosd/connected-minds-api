package com.fernando.connected_minds_api.models;

import java.util.UUID;
import com.fernando.connected_minds_api.models.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fernando.connected_minds_api.enums.InviteType;

@Entity
@Table(name = "invite_table")
@NoArgsConstructor
@Getter
@Setter
public class Invite extends BaseEntity {
    private UUID fromID;
    private InviteType type;

    public Invite(UUID fromID, InviteType type) {
        super(null);
        this.fromID = fromID;
        this.type = type;
    }
}