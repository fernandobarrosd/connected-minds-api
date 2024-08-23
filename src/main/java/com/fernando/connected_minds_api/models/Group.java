package com.fernando.connected_minds_api.models;

import com.fernando.connected_minds_api.models.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "group_table")
@Getter
@Setter
@NoArgsConstructor
public class Group extends BaseEntity {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, name = "photo_url")
    private String photoURL;

    @Column(nullable = false, name = "banner_url")
    private String bannerURL;

    @OneToMany
    private List<Post> posts;

    @ManyToOne
    @JoinColumn(name = "community_id")
    private Community community;

    @ManyToMany
    @JoinTable(
            name = "group_user",
            joinColumns = @JoinColumn(name = "group_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "user_id", nullable = false)
    )
    private List<User> members;

    @OneToMany
    @JoinColumn(name = "group_tag_id")
    private List<Tag> tags;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public Group(String name, String description, String photoURL,
                 String bannerURL, User admin, Community community, List<Tag> tags) {
        super(null);
        this.name = name;
        this.description = description;
        this.photoURL = photoURL;
        this.bannerURL = bannerURL;
        this.community = community;
        this.tags = tags;
        this.members = List.of(admin);
        this.posts = List.of();
        this.createdAt = LocalDateTime.now();
    }
}