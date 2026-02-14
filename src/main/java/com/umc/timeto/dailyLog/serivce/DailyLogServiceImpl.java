package com.umc.timeto.dailyLog.serivce;

import com.umc.timeto.dailyLog.dto.DailyLogMonthlyDTO;
import com.umc.timeto.dailyLog.dto.DailyLogRequestDTO;
import com.umc.timeto.dailyLog.dto.DailyLogResponseDTO;
import com.umc.timeto.dailyLog.entity.DailyLog;
import com.umc.timeto.dailyLog.repository.DailyLogRepository;
import com.umc.timeto.global.apiPayload.code.ErrorCode;
import com.umc.timeto.global.apiPayload.exception.GlobalException;
import com.umc.timeto.member.entity.Member;
import com.umc.timeto.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DailyLogServiceImpl implements DailyLogService {
    private final DailyLogRepository dailyLogRepository;
    private final MemberRepository memberRepository;

    // 일지 등록
    public DailyLogResponseDTO saveLog(DailyLogRequestDTO dto, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND)); // 에러 코드 수정

        LocalDate today = LocalDate.now();
        if (dailyLogRepository.findByMemberAndCreatedAt(member, today).isPresent()) {
            throw new GlobalException(ErrorCode.DUPLICATE_DAILY_LOG);
        }

        DailyLog dailyLog = DailyLog.builder()
                .answer1(dto.getAnswer1())
                .answer2(dto.getAnswer2())
                .answer3(dto.getAnswer3())
                .createdAt(today)
                .member(member)
                .build();

        DailyLog savedLog = dailyLogRepository.save(dailyLog);

        return new DailyLogResponseDTO(
                savedLog.getId(),
                savedLog.getAnswer1(),
                savedLog.getAnswer2(),
                savedLog.getAnswer3(),
                savedLog.getCreatedAt()
        );
    }

    // 월별 일지 조회
    @Transactional(readOnly = true)
    public List<DailyLogMonthlyDTO> getMonthlyLogs(int year, int month, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));

        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        return dailyLogRepository.findByMemberAndCreatedAtBetween(member, start, end)
                .stream()
                .map(log -> new DailyLogMonthlyDTO(log.getId(), log.getCreatedAt(), log.getAnswer1()))
                .toList();
    }

    // 일별 일지 조회
    @Transactional(readOnly = true)
    public DailyLogResponseDTO getDailyLog(Long logId, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));

        DailyLog dailyLog = dailyLogRepository.findById(logId)
                .orElseThrow(() -> new GlobalException(ErrorCode.LOG_NOT_FOUND));

        // 본인 확인
        if (!dailyLog.getMember().getMemberId().equals(member.getMemberId())) {
            throw new GlobalException(ErrorCode.LOG_FORBIDDEN);
        }

        return new DailyLogResponseDTO(
                dailyLog.getId(),
                dailyLog.getAnswer1(),
                dailyLog.getAnswer2(),
                dailyLog.getAnswer3(),
                dailyLog.getCreatedAt()
        );
    }

    // 일지 수정
    public DailyLogResponseDTO updateLog(Long logId, DailyLogRequestDTO dto, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));

        DailyLog dailyLog = dailyLogRepository.findById(logId)
                .orElseThrow(() -> new GlobalException(ErrorCode.LOG_NOT_FOUND));

        if (!dailyLog.getMember().getMemberId().equals(member.getMemberId())) {
            throw new GlobalException(ErrorCode.LOG_FORBIDDEN);
        }

        dailyLog.update(dto.getAnswer1(), dto.getAnswer2(), dto.getAnswer3());

        return new DailyLogResponseDTO(
                dailyLog.getId(),
                dailyLog.getAnswer1(),
                dailyLog.getAnswer2(),
                dailyLog.getAnswer3(),
                dailyLog.getCreatedAt()
        );
    }

    // 일지 삭제
    public Long deleteLog(Long logId, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));

        DailyLog dailyLog = dailyLogRepository.findById(logId)
                .orElseThrow(() -> new GlobalException(ErrorCode.LOG_NOT_FOUND));

        if (!dailyLog.getMember().getMemberId().equals(member.getMemberId())) {
            throw new GlobalException(ErrorCode.LOG_FORBIDDEN);
        }

        dailyLogRepository.delete(dailyLog);
        return logId;
    }
}
