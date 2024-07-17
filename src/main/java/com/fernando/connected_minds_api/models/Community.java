package com.fernando.connected_minds_api.models;

import com.fernando.connected_minds_api.models.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    @OneToMany
    private List<Post> posts;

    @OneToMany
    private List<Message> messages;

    @OneToMany(mappedBy = "community")
    private List<Group> groups;

    @ManyToMany
    private List<User> members;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "community_tag_id", nullable = false)
    private List<Tag> tags;

    @ManyToMany
    @JoinTable(
            name = "community_admin",
            joinColumns = @JoinColumn(name = "community_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "admin_id", nullable = false)
    )
    private List<User> admins;

    public Community(String name, String description, String photoURL, String bannerURL,
                     User admin, List<Tag> tags) {
        super(null);
        this.name = name;
        this.description = description;
        this.photoURL = photoURL;
        this.bannerURL = bannerURL;
        this.tags = tags;
        this.messages = List.of();
        this.members = List.of();
        this.admins = List.of(admin);
        this.posts = List.of();
        this.groups = List.of();

    }
}
