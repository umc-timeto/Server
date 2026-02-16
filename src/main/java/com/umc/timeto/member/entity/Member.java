package com.umc.timeto.member.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "email", nullable = false, length = 100, unique = true)
    private String email;

    @Column(name = "kakao_id", unique = true)
    private Long kakaoId;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    // 카카오 회원 생성
    public static Member createKakaoMember(Long kakaoId, String email, String name) {
        Member member = new Member();
        member.kakaoId = kakaoId;
        member.email = email;
        member.name = name;
        return member;
    }

    // 회원 탈퇴 처리
    public void markDeleted(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    // 회원 복구 처리
    public void restore() {
        this.deletedAt = null;
    }

    // 탈퇴 여부 확인
    public boolean isDeleted() {
        return deletedAt != null;
    }
}