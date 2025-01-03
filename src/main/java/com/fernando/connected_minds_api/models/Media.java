package com.fernando.connected_minds_api.models;

import java.util.UUID;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import com.fernando.connected_minds_api.enums.MediaType;
import com.fernando.connected_minds_api.models.base.BaseEntity;

@Entity
@Table(name = "media_table")
@Getter
@Setter
@NoArgsConstructor
public class Media extends BaseEntity {
    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private MediaType type;


    public Media(UUID id, String name, MediaType type) {
        super(id);
        this.name = name;
        this.type = type;
    }
}