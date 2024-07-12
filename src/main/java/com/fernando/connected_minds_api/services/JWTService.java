package com.fernando.connected_minds_api.services;

import com.auth0.jwt.JWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import static com.auth0.jwt.algorithms.Algorithm.HMAC256;

@Service
public class JWTService {
    @Value("${jwt.secret.key}")
    private String jwtSecretKey;

    private final static String ISSUER = "Connected Minds API Authentication";

    public String generateJWT(String email) {
        return JWT
                .create()
                .withSubject(email)
                .withIssuer(ISSUER)
                .withIssuedAt(Instant.now())
                .withExpiresAt(generateJWTExpirationDate())
                .sign(HMAC256(jwtSecretKey));
    }

    public String generateRefreshToken() {
        return JWT
                .create()
                .withIssuer(ISSUER)
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

    private Instant generateRefreshTokenExpirationDate() {
        return LocalDateTime.now().plusDays(7).toInstant(
                ZoneOffset.of("-03:00")
        );
    }

    private Instant generateJWTExpirationDate() {
        return LocalDateTime.now().plusMinutes(30).toInstant(
                ZoneOffset.of("-03:00")
        );
    }
}