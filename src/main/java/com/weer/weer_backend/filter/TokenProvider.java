package com.weer.weer_backend.filter;


import com.weer.weer_backend.dto.TokenDto;
import com.weer.weer_backend.service.UserDetailsServiceImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SecretKeyBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

  @Value("${spring.jwt.secret}")
  private String SECRETE_KEY;
  private static final String KEY_ROLE = "role";
  private static final long ACCESS_TOKEN_DURATION_IN_SECOND = 1000L * 60 * 60 * 12;
  private static final long REFRESH_TOKEN_DURATION_IN_SECOND = 1000L * 60 * 60 * 24 * 7;
  private final UserDetailsServiceImpl userDetailsService;

  private SecretKey getSigningKey() {
    return new SecretKeySpec(SECRETE_KEY.getBytes(StandardCharsets.UTF_8),
        (((SecretKeyBuilder) Jwts.SIG.HS256.key()).build()).getAlgorithm());
  }

  public TokenDto generateToken(String loginId, Authentication authentication) {
    String role = authentication.getAuthorities().stream().findFirst().get().getAuthority();

    Claims claims = Jwts.claims()
        .setSubject(loginId)
        .add(KEY_ROLE, role)
        .build();

    long now = (new Date()).getTime();
    String accessToken = Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(Date.from(Instant.now()))
        .setExpiration(new Date(now + ACCESS_TOKEN_DURATION_IN_SECOND))
        .signWith(this.getSigningKey())
        .compact();

    String refreshToken = Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(Date.from(Instant.now()))
        .setExpiration(new Date(now + REFRESH_TOKEN_DURATION_IN_SECOND))
        .signWith(this.getSigningKey())
        .compact();

    TokenDto tokenDto = TokenDto.builder()
        .grantType("Bearer")
        .role(role)
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();
    log.info(tokenDto.toString());
    return tokenDto;
  }

  public Authentication getAuthentication(String accessToken) {
    String subject = parseClaims(accessToken).getSubject();
    UserDetails username = userDetailsService.loadUserByUsername(subject);
    return new UsernamePasswordAuthenticationToken(username, accessToken,
        username.getAuthorities());
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser().setSigningKey(this.getSigningKey()).build().parseClaimsJws(token);
      return true;
    } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
      log.info("Invalid JWT Token", e);
    } catch (ExpiredJwtException e) {
      log.info("Expired JWT Token", e);
    } catch (UnsupportedJwtException e) {
      log.info("Unsupported JWT Token", e);
    } catch (IllegalArgumentException e) {
      log.info("JWT claims string is empty.", e);
    }
    return false;
  }

  private Claims parseClaims(String accessToken) {
    try {
      return Jwts.parser()
          .setSigningKey(this.getSigningKey())
          .build()
          .parseClaimsJws(accessToken)
          .getBody();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }

  public String getLoginIdFromToken(String token) {
    Claims claims = Jwts.parser()
        .setSigningKey(this.getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
    return claims.getSubject(); // assuming the subject is the email
  }
}
