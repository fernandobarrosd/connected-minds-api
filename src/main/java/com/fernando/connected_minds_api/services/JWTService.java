package com.fernando.connected_minds_api.services;

import com.auth0.jwt.JWT;
import com.fernando.connected_minds_api.models.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import static com.auth0.jwt.algorithms.Algorithm.HMAC256;

@Service
public class JWTService {
    @Value("${spring.security.secret-key}")
    private String jwtSecretKey;

    private final static String ISSUER = "Connected Minds API Authentication";

    public String generateJWT(User user) {
        return JWT
                .create()
                .withSubject(user.getEmail())
                .withClaim("profile_url", user.getPhotoURL())
                .withClaim("banner_url", user.getBannerURL())
                .withClaim("username", user.getUsername())
                .withClaim("bio", user.getBio())
                .withIssuer(ISSUER)
                .withIssuedAt(Instant.now())
                .withExpiresAt(generateJWTExpirationDate())
                .sign(HMAC256(jwtSecretKey));
    }

    public String generateRefreshToken(UUID userID) {
        return JWT
                .create()
                .withIssuer(ISSUER)
                .withSubject(userID.toString())
                .withIssuedAt(Instant.now())
                .withExpiresAt(generateRefreshTokenExpirationDate())
                .sign(HMAC256(jwtSecretKey));
    }

    public Optional<String> getSubject(String token) {
        try {
            String subject = JWT
                    .require(HMAC256(jwtSecretKey))
                    .build()
                    .verify(token)
                    .getSubject();
            return Optional.of(subject);
        }
        catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<String> getExpiresAt(String token) {
        try {
            Date expiresAt = JWT
                    .require(HMAC256(jwtSecretKey))
                    .build()
                    .verify(token)
                    .getExpiresAt();
            return Optional.of(expiresAt.toString());
        }
        catch (Exception e) {
            return Optional.empty();
        }
    }

    public boolean isValidToken(String token) {
        try {
            JWT.require(HMAC256(jwtSecretKey))
                    .build()
                    .verify(token);
            return true;
        }
        catch (Exception exception) {
            return false;
        }
    }

    private Instant generateRefreshTokenExpirationDate() {
        return LocalDateTime.now().plusMonths(1).toInstant(
                ZoneOffset.of("-03:00")
        );
    }

    private Instant generateJWTExpirationDate() {
        return LocalDateTime.now().plusMinutes(30).toInstant(
                ZoneOffset.of("-03:00")
        );
    }
}