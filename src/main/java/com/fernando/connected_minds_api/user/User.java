package com.fernando.connected_minds_api.user;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name ="user_table")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User implements UserDetails {
    @Id
    private UUID id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "photo_url")
    private String photoURL;

    @Column(name = "banner_url")
    private String bannerURL;

    private String bio;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserGenre genre;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    public User(
        String username, 
        String email, 
        String password, 
        String photoURL, 
        String bannerURL, 
        String bio,
        UserGenre genre,
        LocalDate birthDate) {
        this(UUID.randomUUID(), username, email, password, photoURL, bannerURL, bio, genre, birthDate);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }


}