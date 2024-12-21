package com.fernando.connected_minds_api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fernando.connected_minds_api.enums.UserGenre;
import com.fernando.connected_minds_api.enums.UserStatus;
import com.fernando.connected_minds_api.models.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "user_table")
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({ "communities", "groups" })
public class User extends BaseEntity implements UserDetails {
    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "banner_url")
    private String bannerURL;

    @Column(name = "photo_url")
    private String photoURL;

    private String bio;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserGenre genre;

    @ManyToMany(mappedBy = "members")
    private List<Community> communities;

    @ManyToMany(mappedBy = "members")
    private List<Group> groups;

    @OneToMany(mappedBy = "owner")
    private List<Notification> notifications;

    public User(String username, String email, String password, LocalDate birthDate,
                String bannerURL, String photoURL, String bio, UserGenre genre) {
        super(null);
        this.username = username;
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
        this.bannerURL = bannerURL;
        this.photoURL = photoURL;
        this.bio = bio;
        this.genre = genre;
        this.status = UserStatus.OFFLINE;
        this.communities = List.of();
        this.groups = List.of();
        this.notifications = List.of();
    }

    public User(UUID id, String username, String email, String password, LocalDate birthDate,
                String bannerURL, String photoURL, String bio, UserGenre genre) {
        super(id);
        this.username = username;
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
        this.bannerURL = bannerURL;
        this.photoURL = photoURL;
        this.bio = bio;
        this.genre = genre;
        this.status = UserStatus.OFFLINE;
        this.communities = List.of();
        this.groups = List.of();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
}