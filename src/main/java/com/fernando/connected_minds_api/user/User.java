package com.fernando.connected_minds_api.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Table(name = "user_table")
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true)
    @Setter
    private String username;

    @Column(nullable = false, unique = true)
    @Setter
    private String email;

    @Column(name = "user_password", nullable = false)
    @Setter
    private String password;

    @Column(name = "photo_url")
    @Setter
    private String photoURL;

    @Column(name = "banner_url")
    @Setter
    private String bannerURL;

    @Setter
    private String bio;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Setter
    private UserGenre genre;

    @Column(name = "birth_date", nullable = false)
    @Setter
    private LocalDate birthDate;

    public User(UUID id, String username, String email, String password, String photoURL,
                String bannerURL, String bio, UserGenre genre, LocalDate birthDate) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.photoURL = photoURL;
        this.bannerURL = bannerURL;
        this.bio = bio;
        this.genre = genre;

        validateAge(birthDate);
        this.birthDate = birthDate;
    }

    public User(String username, String email, String password, String photoURL,
                String bannerURL, String bio, UserGenre genre, LocalDate birthDate) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.photoURL = photoURL;
        this.bannerURL = bannerURL;
        this.bio = bio;
        this.genre = genre;

        validateAge(birthDate);
        this.birthDate = birthDate;
    }

    public User(User user) {
        this(user.id, user.username, user.email, user.password,
                user.photoURL, user.bannerURL, user.bio, user.genre, user.birthDate);
    }

    private void validateAge(LocalDate birthDate) {
        int currentYear = LocalDate.now().getYear();
        int birthYear = birthDate.getYear();
        int age = currentYear - birthYear;

        if (age < 13) {
            throw new UserUnderAgeException("User age must be greater than or equal that 13 years old");
        }
    }

    public UserResponse toResponse() {
        return new UserResponse(id, username, birthDate.toString(),
                photoURL, bannerURL, bio, genre
        );
    }

    @Override
    public String toString() {
        return "User(" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", photoURL='" + photoURL + '\'' +
                ", bio='" + bio + '\'' +
                ", genre=" + genre +
                ", birthDate=" + birthDate +
                ')';
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }
}