package com.fernando.connected_minds_api.models.base;

import com.fernando.connected_minds_api.models.User;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@MappedSuperclass
@NoArgsConstructor
@Getter
@Setter
public abstract class Like extends BaseEntity {
    @ManyToOne
    private User owner;

    public Like(User owner) {
        super(null);
        this.owner = owner;
    }
}