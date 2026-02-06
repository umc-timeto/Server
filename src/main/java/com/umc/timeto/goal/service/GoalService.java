package com.umc.timeto.goal.service;

import com.umc.timeto.goal.dto.GoalAddReqDTO;
import com.umc.timeto.goal.dto.GoalUpdateDTO;
import org.springframework.http.ResponseEntity;

public interface GoalService {
    ResponseEntity<?> addGoal(GoalAddReqDTO dto);
    ResponseEntity<?> getGoalList();
    ResponseEntity<?> updateGoal(Long goalId, GoalUpdateDTO dto);
    ResponseEntity<?> deleteGoal(Long goalId);
}
