package com.umc.timeto.goal.repository;

import com.umc.timeto.goal.entity.Goal;
import com.umc.timeto.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {
    List<Goal> findALlByMember(Member member);

    // 회원의 목표 전체 삭제
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from Goal g where g.member.memberId = :memberId")
    void deleteAllByMemberId(@Param("memberId") Long memberId);
}
