package com.umc.timeto.dailyLog.repository;

import com.umc.timeto.dailyLog.entity.DailyLog;
import com.umc.timeto.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyLogRepository extends JpaRepository<DailyLog, Long> {
    Optional<DailyLog> findByMemberAndCreatedAt(Member member, LocalDate createdAt);
    List<DailyLog> findByMemberAndCreatedAtBetween(Member member, LocalDate start, LocalDate end);
}