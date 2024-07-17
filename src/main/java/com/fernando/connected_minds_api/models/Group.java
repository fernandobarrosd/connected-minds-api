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
    private List<Post> posts;

    @ManyToOne
    @JoinColumn(name = "community_id", nullable = false)
    private Community community;

    @OneToMany
    private List<Message> messages;

    @ManyToMany
    private List<User> members;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "group_tag_id", nullable = false)
    private List<Tag> tags;

    @ManyToMany
    @JoinTable(
            name = "group_admin",
            joinColumns = @JoinColumn(name = "group_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "admin_id", nullable = false)
    )
    private List<User> admins;

    public Group(UUID id, String name, String description, String photoURL,
                 String bannerURL, User admin, Community community, List<Tag> tags) {
        super(id);
        this.name = name;
        this.description = description;
        this.photoURL = photoURL;
        this.bannerURL = bannerURL;
        this.community = community;
        this.tags = tags;
        this.messages = List.of();
        this.members = List.of();
        this.admins = List.of(admin);
        this.posts = List.of();

    }
}
