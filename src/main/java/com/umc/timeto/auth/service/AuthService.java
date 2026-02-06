package com.umc.timeto.auth.service;

import com.umc.timeto.auth.client.KakaoClient;
import com.umc.timeto.auth.dto.KakaoLoginResponse;
import com.umc.timeto.auth.dto.kakao.KakaoUserInfo;
import com.umc.timeto.auth.entity.RefreshToken;
import com.umc.timeto.auth.jwt.JwtProvider;
import com.umc.timeto.auth.repository.RefreshTokenRepository;
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
    private final RefreshTokenRepository refreshTokenRepository;

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

        // refreshToken DB 저장(회원당 1개 upsert)
        RefreshToken savedToken = refreshTokenRepository.findById(member.getMemberId())
                .orElseGet(() -> RefreshToken.create(member.getMemberId(), refreshToken));

        savedToken.updateToken(refreshToken);
        refreshTokenRepository.save(savedToken);

        KakaoLoginResponse response = new KakaoLoginResponse(
                member.getMemberId(),
                accessToken,
                refreshToken
        );

        return new LoginResult(response, isNewMember);
    }

    // 로그아웃(리프레시 토큰 삭제로 무효화)
    @Transactional
    public void logout(String accessToken) {
        if (!jwtProvider.validateToken(accessToken)) {
            throw new IllegalArgumentException("Invalid access token");
        }

        Long memberId = jwtProvider.getMemberId(accessToken);
        refreshTokenRepository.deleteById(memberId);
    }

    public record LoginResult(
            KakaoLoginResponse response,
            boolean isNewMember
    ) { }
}
