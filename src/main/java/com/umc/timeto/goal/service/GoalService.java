package com.umc.timeto.goal.service;

import com.umc.timeto.goal.dto.GoalAddReqDTO;
import com.umc.timeto.goal.dto.GoalUpdateDTO;
import com.umc.timeto.member.entity.Member;
import org.springframework.http.ResponseEntity;

public interface GoalService {
    ResponseEntity<?> addGoal(GoalAddReqDTO dto, Member member);
    ResponseEntity<?> getGoalList(Member member);
    ResponseEntity<?> updateGoal(Long goalId, GoalUpdateDTO dto, Member member);
    ResponseEntity<?> deleteGoal(Long goalId, Member member);
}
