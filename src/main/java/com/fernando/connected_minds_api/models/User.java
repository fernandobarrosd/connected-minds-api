package com.fernando.connected_minds_api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fernando.connected_minds_api.enums.UserGenre;
import com.fernando.connected_minds_api.enums.UserStatus;
import com.fernando.connected_minds_api.enums.converters.UserGenreConverter;
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

    @Column(nullable = false, name = "photo_url")
    private String photoURL;

    private String bio;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(nullable = false)
    @Convert(converter = UserGenreConverter.class)
    private UserGenre genre;

    @ManyToMany(mappedBy = "members")
    private List<Community> communities;

    @ManyToMany
    @JoinTable(
            name = "user_group",
            joinColumns = @JoinColumn(name = "user_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "group_id", nullable = false)
    )
    private List<Group> groups;



    public User(String username, String email, String password, LocalDate birthDate,
                String bannerURL, String photoURL, UserGenre genre) {
        super(null);
        this.username = username;
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
        this.bannerURL = bannerURL;
        this.photoURL = photoURL;
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