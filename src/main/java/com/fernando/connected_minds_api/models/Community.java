package com.fernando.connected_minds_api.models;

import com.fernando.connected_minds_api.models.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;
import java.util.UUID;

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

    @OneToMany(mappedBy = "community")
    private List<Group> groups;

    @ManyToMany
    private List<User> members;

    @ManyToMany
    @JoinTable(
            name = "community_admin",
            joinColumns = @JoinColumn(name = "community_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "admin_id", nullable = false)
    )
    private List<User> admins;

    public Community(UUID id, String name, String description, String photoURL, String bannerURL,
                     User admin) {
        super(id);
        this.name = name;
        this.description = description;
        this.photoURL = photoURL;
        this.bannerURL = bannerURL;
        this.members = List.of();
        this.admins = List.of(admin);
        this.posts = List.of();
        this.groups = List.of();

    }
}