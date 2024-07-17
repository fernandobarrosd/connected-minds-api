package com.fernando.connected_minds_api.models;

import com.fernando.connected_minds_api.models.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tag_table")
@NoArgsConstructor
@Getter
@Setter
public class Tag extends BaseEntity {
    @Column(nullable = false)
    private String name;

    public Tag(String name) {
        super(null);
        this.name = name;
    }
}