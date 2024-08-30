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
@Table(name = "like_post_table")
@NoArgsConstructor
@Getter
@Setter
public class LikePost extends Like {
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public LikePost(User owner, Post post) {
        super(owner);
        this.post = post;
    }
}