package com.fernando.connected_minds_api.models;

import com.fernando.connected_minds_api.models.base.Like;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "like_comment_table")
@NoArgsConstructor
@Getter
@Setter
public class LikeComment extends Like {
    @ManyToOne
    @JoinColumn(name = "like_comment_table", nullable = false)
    private Comment comment;

    public LikeComment(User owner, Comment comment) {
        super(owner);
        this.comment = comment;
    }
}