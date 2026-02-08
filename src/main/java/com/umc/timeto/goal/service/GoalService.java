package com.umc.timeto.goal.service;

import com.umc.timeto.goal.dto.GoalAddReqDTO;
import com.umc.timeto.goal.dto.GoalUpdateDTO;
import org.springframework.http.ResponseEntity;

public interface GoalService {
    ResponseEntity<?> addGoal(GoalAddReqDTO dto, Long memberId);
    ResponseEntity<?> getGoalList(Long memberId);
    ResponseEntity<?> updateGoal(Long goalId, GoalUpdateDTO dto, Long memberId);
    ResponseEntity<?> deleteGoal(Long goalId, Long memberId);
}
