package com.umc.timeto.auth.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    private Long memberId;

    @Column(nullable = false, length = 500)
    private String token;

    private RefreshToken(Long memberId, String token) {
        this.memberId = memberId;
        this.token = token;
    }

    // 리프레시 토큰 생성
    public static RefreshToken create(Long memberId, String token) {
        return new RefreshToken(memberId, token);
    }

    // 리프레시 토큰 갱신
    public void updateToken(String token) {
        this.token = token;
    }
}
