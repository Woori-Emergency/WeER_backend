package com.weer.weer_backend.filter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;


@Component
public class JWTUtil {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(JWTUtil.class);

    private final SecretKey secretKey;

    public JWTUtil(@Value("${spring.jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String getLoginId(String token) {
        return Jwts.parser()
                    .verifyWith(this.secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get("username", String.class);
    }

    public Boolean isExpired(String token) {
        return Jwts.parser()
                    .verifyWith(this.secretKey)
                    .build().parseSignedClaims(token)
                    .getPayload()
                    .getExpiration().before(new Date());
    }

    public String createJwt(String loginId, String role, long expiredMs) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiredMs);

        return Jwts.builder()
                .claim("sub",loginId)
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }
}

