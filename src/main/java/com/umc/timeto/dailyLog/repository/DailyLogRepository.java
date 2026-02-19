package com.umc.timeto.dailyLog.repository;

import com.umc.timeto.dailyLog.entity.DailyLog;
import com.umc.timeto.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyLogRepository extends JpaRepository<DailyLog, Long> {
    List<DailyLog> findByMemberAndDateBetweenOrderByDateAscIdAsc(Member member, LocalDate start, LocalDate end);

    // 회원의 일지 전체 삭제
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from DailyLog d where d.member.memberId = :memberId")
    void deleteAllByMemberId(@Param("memberId") Long memberId);
}