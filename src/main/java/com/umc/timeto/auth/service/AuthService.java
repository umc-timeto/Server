package com.umc.timeto.auth.service;

import com.umc.timeto.auth.client.KakaoClient;
import com.umc.timeto.auth.dto.KakaoLoginResponse;
import com.umc.timeto.auth.dto.kakao.KakaoUserInfo;
import com.umc.timeto.auth.jwt.JwtProvider;
import com.umc.timeto.member.entity.Member;
import com.umc.timeto.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final KakaoClient kakaoClient;
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    // 카카오 로그인(신규면 회원 생성)
    @Transactional
    public LoginResult kakaoLogin(String authorizationCode) {

        String kakaoAccessToken = kakaoClient.getAccessToken(authorizationCode);
        KakaoUserInfo userInfo = kakaoClient.getUserInfo(kakaoAccessToken);

        Optional<Member> existingMember = memberRepository.findByKakaoId(userInfo.getKakaoId());

        boolean isNewMember;
        Member member;

        if (existingMember.isPresent()) {
            member = existingMember.get();
            isNewMember = false;
        } else {
            member = memberRepository.save(
                    Member.createKakaoMember(
                            userInfo.getKakaoId(),
                            userInfo.getEmail(),
                            userInfo.getName()
                    )
            );
            isNewMember = true;
        }

        String accessToken = jwtProvider.createAccessToken(member.getMemberId());
        String refreshToken = jwtProvider.createRefreshToken(member.getMemberId());

        KakaoLoginResponse response = new KakaoLoginResponse(
                member.getMemberId(),
                accessToken,
                refreshToken
        );

        return new LoginResult(response, isNewMember);
    }

    public record LoginResult(
            KakaoLoginResponse response,
            boolean isNewMember
    ) { }
}
