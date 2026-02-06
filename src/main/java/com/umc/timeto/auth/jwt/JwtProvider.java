package com.umc.timeto.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
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
        signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    // access token 발급
    public String createAccessToken(Long memberId) {
        return createToken(memberId, accessExpSeconds);
    }

    // refresh token 발급
    public String createRefreshToken(Long memberId) {
        return createToken(memberId, refreshExpSeconds);
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("validateToken error = " + e.getClass().getName() + " / " + e.getMessage());
            return false;
        }
    }

    // 토큰에서 memberId 추출
    public Long getMemberId(String token) {
        Claims claims = parseClaims(token);
        return Long.valueOf(claims.getSubject());
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
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
