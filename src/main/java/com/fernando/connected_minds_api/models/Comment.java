package com.fernando.connected_minds_api.models;

import com.fernando.connected_minds_api.models.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "comment_table")
@NoArgsConstructor
@Getter
@Setter
public class Comment extends BaseEntity {
    @Column(nullable = false)
    private String content;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "onwer_id")
    private User owner;

    @ManyToOne
    private Post post;

    @OneToMany
    @JoinColumn(name = "comment_id")
    private List<Comment> comments;

    @Column(nullable = false)
    private Long likes;

    public Comment(String content, User owner, Post post) {
        super(null);
        this.content = content;
        this.owner = owner;
        this.post = post;
        this.createdAt = LocalDateTime.now();
        this.likes = 0L;
        this.comments = List.of();
    }
}