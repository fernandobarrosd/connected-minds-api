package com.fernando.connected_minds_api.models;

import com.fernando.connected_minds_api.models.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

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
    @JoinColumn(name = "post_id", nullable = false)
    private List<Post> posts;

    @ManyToOne
    @JoinColumn(name = "community_id", nullable = false)
    private Community community;

    @ManyToMany
    private List<User> members;

    @ManyToMany
    @JoinTable(
            name = "group_admin",
            joinColumns = @JoinColumn(name = "group_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "admin_id", nullable = false)
    )
    private List<User> admins;

    public Group(UUID id, String name, String description, String photoURL,
                 String bannerURL, User admin, Community community) {
        super(id);
        this.name = name;
        this.description = description;
        this.photoURL = photoURL;
        this.bannerURL = bannerURL;
        this.community = community;
        this.members = List.of();
        this.admins = List.of(admin);
        this.posts = List.of();

    }
}