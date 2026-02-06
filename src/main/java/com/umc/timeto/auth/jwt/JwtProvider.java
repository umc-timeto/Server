package com.umc.timeto.auth.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.access-exp-seconds}")
    private long accessExpSeconds;

    @Value("${jwt.refresh-exp-seconds}")
    private long refreshExpSeconds;

    private Key signingKey;

    @PostConstruct
    public void init() {
        signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // access token 발급
    public String createAccessToken(Long memberId) {
        return createToken(memberId, accessExpSeconds);
    }

    // refresh token 발급
    public String createRefreshToken(Long memberId) {
        return createToken(memberId, refreshExpSeconds);
    }

    private String createToken(Long memberId, long expSeconds) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + (expSeconds * 1000L));

        return Jwts.builder()
                .setSubject(String.valueOf(memberId))
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }
}
