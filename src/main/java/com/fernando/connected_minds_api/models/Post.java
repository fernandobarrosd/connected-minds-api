package com.fernando.connected_minds_api.models;

import com.fernando.connected_minds_api.models.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "post_table")
@NoArgsConstructor
@Getter
@Setter
public class Post extends BaseEntity {
    @Column(nullable = false)
    private String content;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "photo_url")
    private String photoURL;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(nullable = false)
    private Long likes;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

    public Post(String content, String photoURL, User owner) {
        super(null);
        this.content = content;
        this.photoURL = photoURL;
        this.owner = owner;
        this.createdAt = LocalDateTime.now();
        this.likes = 0L;
        this.comments = List.of();
    }
}