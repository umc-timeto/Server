package com.umc.timeto.auth.service;

import com.umc.timeto.auth.client.KakaoClient;
import com.umc.timeto.auth.dto.KakaoLoginResponse;
import com.umc.timeto.auth.dto.TokenRefreshResponse;
import com.umc.timeto.auth.dto.kakao.KakaoUserInfo;
import com.umc.timeto.auth.entity.RefreshToken;
import com.umc.timeto.auth.jwt.JwtProvider;
import com.umc.timeto.auth.repository.RefreshTokenRepository;
import com.umc.timeto.dailyLog.repository.DailyLogRepository;
import com.umc.timeto.folder.repository.FolderRepository;
import com.umc.timeto.global.apiPayload.code.ErrorCode;
import com.umc.timeto.global.apiPayload.exception.GlobalException;
import com.umc.timeto.goal.repository.GoalRepository;
import com.umc.timeto.member.entity.Member;
import com.umc.timeto.member.repository.MemberRepository;
import com.umc.timeto.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final KakaoClient kakaoClient;
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    private static final int deleteGraceDays = 14;

    private final DailyLogRepository dailyLogRepository;
    private final TodoRepository todoRepository;
    private final FolderRepository folderRepository;
    private final GoalRepository goalRepository;

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

            // 회원탈퇴(소프트딜리트)된 계정이라면 14일 내 재로그인 시 복구
            restoreIfWithinGracePeriod(member);

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
            throw new GlobalException(ErrorCode.AUTH_INVALID_TOKEN);
        }

        Long memberId = jwtProvider.getMemberId(accessToken);
        refreshTokenRepository.deleteById(memberId);
    }

    public record LoginResult(
            KakaoLoginResponse response,
            boolean isNewMember
    ) { }

    //  accessToken 재발급
    @Transactional
    public TokenRefreshResponse refresh(String refreshToken) {
        if (!jwtProvider.validateToken(refreshToken)) {
            throw new GlobalException(ErrorCode.AUTH_INVALID_TOKEN);
        }

        Long memberId = jwtProvider.getMemberId(refreshToken);

        RefreshToken savedToken = refreshTokenRepository.findById(memberId)
                .orElseThrow(() -> new GlobalException(ErrorCode.AUTH_REFRESH_TOKEN_NOT_FOUND));

        if (!savedToken.getToken().equals(refreshToken)) {
            throw new GlobalException(ErrorCode.AUTH_REFRESH_TOKEN_MISMATCH);
        }

        String newAccessToken = jwtProvider.createAccessToken(memberId);

        return new TokenRefreshResponse(memberId, newAccessToken, refreshToken);
    }

    // 회원 탈퇴 요청(소프트 딜리트 14일)
    @Transactional
    public void requestDelete(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));

        if (member.isDeleted()) {
            throw new GlobalException(ErrorCode.BAD_REQUEST);
        }

        member.markDeleted(LocalDateTime.now());
        refreshTokenRepository.deleteById(memberId);
    }

    // 탈퇴 계정 14일 내 재로그인 시 복구
    private void restoreIfWithinGracePeriod(Member member) {
        if (!member.isDeleted()) {
            return;
        }

        LocalDateTime deletedAt = member.getDeletedAt();
        if (deletedAt == null) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime restoreDeadline = deletedAt.plusDays(deleteGraceDays);

        if (now.isBefore(restoreDeadline) || now.isEqual(restoreDeadline)) {
            member.restore();
        }
    }

    // 회원 완전 삭제(스케줄러에서 호출)
    @Transactional
    public void hardDeleteMember(Long memberId) {

        // 1) member FK 가진 테이블들 먼저 삭제
        dailyLogRepository.deleteAllByMemberId(memberId);

        // 2) goal → folder → todo 방향으로 FK 걸려있으니, 역순으로 삭제
        //    todo 삭제 시 block은 orphanRemoval로 자동 삭제됨
        todoRepository.deleteAllByMemberId(memberId);
        folderRepository.deleteAllByMemberId(memberId);
        goalRepository.deleteAllByMemberId(memberId);

        // 3) auth 토큰 삭제
        refreshTokenRepository.deleteById(memberId);

        // 4) 마지막에 member 삭제
        memberRepository.deleteById(memberId);
    }
}
