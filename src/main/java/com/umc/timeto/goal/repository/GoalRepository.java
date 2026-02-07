package com.umc.timeto.goal.repository;

import com.umc.timeto.goal.entity.Goal;
import com.umc.timeto.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {
    List<Goal> findALlByMember(Member member);
}
