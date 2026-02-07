package com.umc.timeto.member.repository;

import com.umc.timeto.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    //카카오 아이디로 회원 조회
    Optional<Member> findByKakaoId(Long kakaoId);

    // 이메일로 회원 조회
    Optional<Member> findByEmail(String email);
}
