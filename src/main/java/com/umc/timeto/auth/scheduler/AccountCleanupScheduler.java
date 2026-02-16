package com.umc.timeto.auth.scheduler;

import com.umc.timeto.auth.service.AuthService;
import com.umc.timeto.member.entity.Member;
import com.umc.timeto.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AccountCleanupScheduler {

    private final MemberRepository memberRepository;
    private final AuthService authService;

    private static final int deleteGraceDays = 14;

    // 탈퇴 2주 경과 회원 완전 삭제
    @Scheduled(cron = "0 0 3 * * *") // 매일 03:00
    public void hardDeleteExpiredMembers() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(deleteGraceDays);
        List<Member> expiredMembers =
                memberRepository.findAllByDeletedAtIsNotNullAndDeletedAtBefore(cutoff);

        for (Member member : expiredMembers) {
            authService.hardDeleteMember(member.getMemberId());
        }
    }
}
