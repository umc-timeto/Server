package com.umc.timeto.member.entity;
import jakarta.persistence.*;
import lombok.*;

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

    public static Member createKakaoMember(Long kakaoId, String email, String name) {
        Member member = new Member();
        member.kakaoId = kakaoId;
        member.email = email;
        member.name = name;
        return member;
    }
}