package com.fernando.connected_minds_api.models;

import com.fernando.connected_minds_api.models.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "community_table")
@NoArgsConstructor
@Getter
@Setter
public class Community extends BaseEntity {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, name = "photo_url")
    private String photoURL;

    @Column(nullable = false, name = "banner_url")
    private String bannerURL;

    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany
    private List<Post> posts;

    @OneToMany(mappedBy = "community")
    private List<Group> groups;

    @ManyToMany
    @JoinTable(
            name = "community_user",
            joinColumns = @JoinColumn(name = "community_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "user_id", nullable = false)
    )
    private List<User> members;

    @OneToMany
    @JoinColumn(name = "community_tag_id")
    private List<Tag> tags;

    public Community(String name, String description, String photoURL, String bannerURL,
                     User admin, List<Tag> tags) {
        super(null);
        this.name = name;
        this.description = description;
        this.photoURL = photoURL;
        this.bannerURL = bannerURL;
        this.tags = tags;
        this.createdAt = LocalDateTime.now();
        this.members = List.of(admin);
        this.posts = List.of();
        this.groups = List.of();

    }
}
